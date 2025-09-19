package org.mbg.anm.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.common.base.model.ProducerRequest;
import org.mbg.common.queue.RedisQueueFactory;
import org.mbg.common.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiConsumerJob {
    private final RedisQueueFactory redisQueueFactory;

    private final ApiConsumerProperties apiConsumerProperties;

    private final GroupIbApiSender groupIbApiSender;

    private final TaskExecutor taskExecutor;

    public ApiConsumerJob(ApiConsumerProperties props, RedisQueueFactory  redisQueueFactory,
                       GroupIbApiSender groupIbApiSender,
                       @Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
        this.apiConsumerProperties = props;
        this.taskExecutor = taskExecutor;
        this.redisQueueFactory = redisQueueFactory;
        this.groupIbApiSender = groupIbApiSender;
    }

    @PostConstruct
    public void init() {
        if (Validator.isNotNull(apiConsumerProperties.getGroups())) {
            apiConsumerProperties.getGroups().forEach(group -> {

            });
        }
    }

    protected void craw(ProducerRequest producerRequest) {

    }

}
