package org.mbg.anm.configuration;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.consumer.request.GroupIbReq;
import org.mbg.anm.consumer.response.GroupIbCompromisedRes;
import org.mbg.anm.queue.LookUpDataMessageListener;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.base.enums.CustomerDataType;
import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.LeakSeverity;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.util.DateUtil;
import org.mbg.common.util.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ApiConsumerJob {

    private final ApiConsumerProperties apiConsumerProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    private final GroupIbApiSender groupIbApiSender;

    private final TaskExecutor taskExecutor;

    private final RecordRepository recordRepository;

    private final CustomerRepository customerRepository;

    private final CustomerDataRepository customerDataRepository;

    private final Gson gson;

    public ApiConsumerJob(ApiConsumerProperties props,
                       GroupIbApiSender groupIbApiSender, RecordRepository recordRepository,
                       CustomerRepository customerRepository, CustomerDataRepository customerDataRepository,
                       Gson gson, RedisTemplate<String, Object> redisTemplate,
                       @Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
        this.apiConsumerProperties = props;
        this.taskExecutor = taskExecutor;
        this.groupIbApiSender = groupIbApiSender;
        this.recordRepository = recordRepository;
        this.customerRepository = customerRepository;
        this.customerDataRepository = customerDataRepository;
        this.gson = gson;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        if (Validator.isNotNull(apiConsumerProperties.getGroups())) {
            _log.info("start create consumers");
            apiConsumerProperties.getGroups().forEach(group -> {
                group.getAccounts().forEach(account -> {
                    _log.info("create consumer {} - account: {} - topic: {}", group.getDataSource(), account.getUser(), group.getTopic());
                    LookUpDataMessageListener worker = new LookUpDataMessageListener(
                            (item) -> {
                                this.craw((Long) item.getPayload(), group.getApi(),
                                        HeaderUtil.getBasicAuthorization(account.getUser(), account.getKey()),
                                        group.getDataSource());
                            },
                            redisTemplate,
                            taskExecutor,
                            account.getUser(),
                            group.getGroupName(),
                            group.getTopic());
                    worker.start();
                });
            });
        }
    }

    @Transactional
    protected void craw(Long id, String api , String token, String dataSource) {
        if (Validator.isNull(id)) {
            return;
        }

        CustomerData dataItem = this.customerDataRepository.findByIdAndStatus(id, EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(dataItem)) {
            return;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        StringBuilder filter = new StringBuilder();
        if (Validator.equals(dataItem.getType(), CustomerDataType.EMAIL.getValue())) {
            filter.append("email:");
        }
        if (Validator.equals(dataItem.getType(), CustomerDataType.PHONE.getValue())) {
            filter.append("phone:");
        }
        filter.append(dataItem.getValue());

        GroupIbReq groupIbReq = new GroupIbReq();
        groupIbReq.setMethod(HttpMethod.GET);
        groupIbReq.setBaseUrl(api);

        params.add("q", filter.toString());
        params.add("offset", "0");
        params.add("limit", "100");

        GroupIbCompromisedRes response =  groupIbApiSender.sendToGroupIb(groupIbReq,
                GroupIbCompromisedRes.class, params, token);

        if (Validator.isNull(response)) {
            return;
        }

        if (Validator.isNotNull(response.getItems())) {
            List<Record> records = new ArrayList<>();
            response.getItems().forEach(item -> {
                String leakId = response.getResultId();
                if (!recordRepository.existsByCustomerIdAndLeakId(dataItem.getCustomerId(), leakId)) {
                    Record record = new Record(dataItem.getCustomerId() , leakId, Validator.isNotNull(item.getId()) ? item.getId().getFirst() : null, dataSource,
                            item.getLeakName(), DateUtil.utcToTimeStampSecond(item.getLeakPublished()),
                            DateUtil.utcToTimeStampSecond(item.getUploadTime()),
                            Validator.isNotNull(item.getEvaluation()) ? resolveSeverityGroupIb(item.getEvaluation().getSeverity()) : null ,
                            this.gson.fromJson(item.getAddInfo(), new TypeToken<Map<String, Object>>(){}.getType()),
                            item.getDescription()
                    );

                    records.add(record);
                }
            });

            this.recordRepository.saveAll(records);
        }

        dataItem.setSyncStatus(CustomerSyncStatus.UPDATED.getStatus());
        dataItem.setLastScan(LocalDateTime.now());

        this.customerDataRepository.save(dataItem);
    }

    private Integer resolveSeverityGroupIb(String input) {
        return switch (input) {
            case "green" -> LeakSeverity.LOW.getValue();
            case "yellow" -> LeakSeverity.MEDIUM.getValue();
            case "orange" -> LeakSeverity.HIGH.getValue();
            case "red" -> LeakSeverity.CRITICAL.getValue();
            default -> null;
        };

    }

}
