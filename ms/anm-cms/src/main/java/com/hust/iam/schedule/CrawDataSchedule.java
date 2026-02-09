package com.hust.iam.schedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import com.hust.iam.configuration.MessageListenerProperties;
import com.hust.iam.queue.LookupMessageProducer;
import com.hust.common.base.enums.CustomerSyncStatus;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.model.CustomerData;
import com.hust.common.base.repository.CustomerDataRepository;
import com.hust.common.base.schedule.Worker;
import com.hust.common.model.RedisMessage;
import com.hust.common.util.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Transactional
@ConditionalOnProperty(prefix = "scheduling.craw-data", name = "enabled", havingValue = "true", matchIfMissing = false)
public class CrawDataSchedule implements Worker {
    private final LookupMessageProducer lookupMessageProducer;

    private final CustomerDataRepository customerDataRepository;

    private final MessageListenerProperties properties;


    private final TaskExecutor taskExecutor;

    public CrawDataSchedule(LookupMessageProducer lookupMessageProducer, CustomerDataRepository customerDataRepository,
                            MessageListenerProperties properties, @Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
        this.lookupMessageProducer = lookupMessageProducer;
        this.customerDataRepository = customerDataRepository;
        this.properties = properties;
        this.taskExecutor = taskExecutor;
    }

    @Override
    @Scheduled(cron = "${scheduling.craw-data.cron}")
    @SchedulerLock(name = "${scheduling.craw-data.name:craw-data}",
            lockAtLeastFor = "${scheduling.craw-data.lock-at-least}",
            lockAtMostFor = "${scheduling.craw-data.lock-at-most}")
    @Async("scheduleExecutor")
    public void run() {
        _log.info("start CrawDataSchedule at {}", new Date());
        Integer pushMessageLimit = properties.getLookupMessageLimit();
        List<CustomerData> data = customerDataRepository.getLookupData(CustomerSyncStatus.NEW, EntityStatus.ACTIVE, pushMessageLimit);

        if (data.size() < pushMessageLimit) {
            List<CustomerData> extend = customerDataRepository.getLookupData(CustomerSyncStatus.UPDATED, EntityStatus.ACTIVE,pushMessageLimit - data.size());
            data.addAll(extend);
        }

        _log.info("CrawDataSchedule found {} items", data.size());

        if (Validator.isNotNull(data)) {
            List<RedisMessage> ids = new ArrayList<>(data.size());

            data.parallelStream().forEach(producerRequest -> {
                producerRequest.setSyncStatus(CustomerSyncStatus.WAITING.getStatus());
                ids.add(RedisMessage.of(producerRequest.getId()) );
            });

            this.customerDataRepository.saveAll(data);
            this.customerDataRepository.flush();

            try {
                this.taskExecutor.execute(() -> {
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                    }
                    lookupMessageProducer.publishBatch(ids);
                });
            } catch (Exception e) {
                _log.error("CrawDataSchedule producer occurred an exception {}", e.getMessage());
            }
        }

        _log.info("end CrawDataSchedule at {} with {} record", new Date(), Validator.isNotNull(data) ? data.size() : 0);
    }
}
