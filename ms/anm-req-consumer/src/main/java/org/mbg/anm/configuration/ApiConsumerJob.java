package org.mbg.anm.configuration;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.consumer.request.GroupIbReq;
import org.mbg.anm.consumer.response.GroupIbCompromisedRes;
import org.mbg.anm.consumer.response.GroupIbResponse;
import org.mbg.anm.queue.RedisPriorityMessageWorker;
import org.mbg.anm.repository.ProducerRequestRepository;
import org.mbg.anm.repository.RecordRepository;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.base.enums.LeakSeverity;
import org.mbg.common.base.model.ProducerRequest;
import org.mbg.common.base.model.Record;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.queue.RedisQueueFactory;
import org.mbg.common.util.DateUtil;
import org.mbg.common.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ApiConsumerJob {
    private final RedisQueueFactory redisQueueFactory;

    private final ApiConsumerProperties apiConsumerProperties;

    private final GroupIbApiSender groupIbApiSender;

    private final TaskExecutor taskExecutor;

    private final RecordRepository recordRepository;

    private final ProducerRequestRepository producerRequestRepository;

    private final Gson gson;

    public ApiConsumerJob(ApiConsumerProperties props, RedisQueueFactory  redisQueueFactory,
                       GroupIbApiSender groupIbApiSender, RecordRepository recordRepository,
                       ProducerRequestRepository producerRequestRepository, Gson gson,
                       @Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
        this.apiConsumerProperties = props;
        this.taskExecutor = taskExecutor;
        this.redisQueueFactory = redisQueueFactory;
        this.groupIbApiSender = groupIbApiSender;
        this.recordRepository = recordRepository;
        this.producerRequestRepository = producerRequestRepository;
        this.gson = gson;
    }

    @PostConstruct
    public void init() {
        if (Validator.isNotNull(apiConsumerProperties.getGroups())) {
            _log.info("start create consumers");
            apiConsumerProperties.getGroups().forEach(group -> {
                group.getAccounts().forEach(account -> {
                    _log.info("create consumer {} - account: {} - topic: {}", group.getDataSource(), account.getUser(), group.getTopic());
                    RedisPriorityMessageWorker worker = new RedisPriorityMessageWorker(
                            this::craw,
                            redisQueueFactory,
                            taskExecutor,
                            group.getTopic(),
                            () -> HeaderUtil.getBasicAuthorization(account.getUser(), account.getKey()),
                            group.getDataSource(),
                            group.getApi());
                    worker.start();
                });
            });
        }
    }

    protected void craw(String api, String token, String dataSource, RedisMessage message) {
        Long id = (Long) message.getPayload();

        if (Validator.isNull(id)) {
            return;
        }

        ProducerRequest req = this.producerRequestRepository.findById(id).orElse(null);

        if (Validator.isNull(req)) {
            return;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        StringBuilder filter = new StringBuilder();
        if (Validator.isNotNull(req.getEmail())) {
            filter.append("email:").append(req.getEmail());
        }

        if (Validator.isNotNull(req.getPhone())) {
            if (!filter.isEmpty()) {
                filter.append(" OR ");
            }
            filter.append("phone:").append(req.getPhone());
        }

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
                Record record = new Record(req.getRequestId(), Validator.isNotNull(item.getId()) ? item.getId().getFirst() : null, dataSource,
                        item.getLeakName(), DateUtil.utcToTimeStampSecond(item.getLeakPublished()),
                        DateUtil.utcToTimeStampSecond(item.getUploadTime()),
                        Validator.isNotNull(item.getEvaluation()) ? resolveSeverityGroupIb(item.getEvaluation().getSeverity()) : null ,
                        this.gson.fromJson(item.getAddInfo(), new TypeToken<Map<String, Object>>(){}.getType()),
                        item.getDescription()
                        );

                records.add(record);
            });

            this.recordRepository.saveAll(records);
        }
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
