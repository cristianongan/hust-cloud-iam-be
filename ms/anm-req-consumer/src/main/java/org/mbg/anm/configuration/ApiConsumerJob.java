package org.mbg.anm.configuration;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.consumer.request.GroupIbReq;
import org.mbg.anm.consumer.response.GroupIbCompromisedRes;
import org.mbg.anm.queue.LookUpDataMessageListener;
import org.mbg.anm.service.CustomerDataService;
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

    private final TaskExecutor taskExecutor;

    private final CustomerDataService customerDataService;

    public ApiConsumerJob(ApiConsumerProperties props,RedisTemplate<String, Object> redisTemplate,
                       CustomerDataService customerDataService,
                       @Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
        this.apiConsumerProperties = props;
        this.taskExecutor = taskExecutor;
        this.redisTemplate = redisTemplate;
        this.customerDataService = customerDataService;
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
                                this.customerDataService.craw((Long) item.getPayload(), group.getApi(),
                                        HeaderUtil.getBasicAuthorization(account.getUser(), account.getKey()),
                                        group.getDataSource());
                            },
                            (item) -> {
                                this.customerDataService.onFail((Long) item.getPayload());
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



}
