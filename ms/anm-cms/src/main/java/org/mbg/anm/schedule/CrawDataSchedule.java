package org.mbg.anm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.mbg.anm.repository.ProducerRequestRepository;
import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.ProducerRequest;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.base.schedule.Worker;
import org.mbg.common.configuration.RedisQueueProperties;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.queue.Producer;
import org.mbg.common.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
@ConditionalOnProperty(prefix = "scheduling.craw-data", name = "enabled", havingValue = "true", matchIfMissing = false)
public class CrawDataSchedule implements Worker {

//    private final ProducerRequestRepository producerRequestRepository;

    private final CustomerRepository customerRepository;

    private final Producer producer;

    private final RedisQueueProperties redisQueueProperties;

    @Value("${scheduling.craw-data.push-message-limit}")
    private int pushMessageLimit;


    @Override
    @Scheduled(cron = "${scheduling.craw-data.cron}")
    @SchedulerLock(name = "craw-data",
            lockAtLeastFor = "${scheduling.craw-data.lock-at-least}",
            lockAtMostFor = "${scheduling.craw-data.lock-at-most}")
    @Async("scheduleExecutor")
    public void run() {
        _log.info("start CrawDataSchedule at {}", new Date());
//        List<ProducerRequest> data = this.producerRequestRepository.findByStatusLimit(CustomerSyncStatus.NEW.getStatus(), pushMessageLimit);
        List<Customer> data = customerRepository.findBySyncStatusAndLimitOrderCreated(CustomerSyncStatus.NEW.getStatus(), pushMessageLimit);

        if (data.size() < pushMessageLimit) {
            List<Customer> extend = customerRepository.findBySyncStatusAndLimitOrderLastSync(CustomerSyncStatus.UPDATED.getStatus(), pushMessageLimit - data.size());
            data.addAll(extend);
        }

        _log.info("CrawDataSchedule found {} items", data.size());

        if (Validator.isNotNull(data)) {
            data.parallelStream().forEach(producerRequest -> {
                try {
                    RedisMessage redisMessage = RedisMessage.of(producerRequest.getId(), redisQueueProperties.getGroupIbRequestTopic(), 0);

                    producer.sendPriorityMessage(redisMessage);

                    producerRequest.setStatus(CustomerSyncStatus.WAITING.getStatus());
                } catch (Exception e) {
                    _log.error("CrawDataSchedule producer occurred an exception {}", e.getMessage());
                }
            });

            this.customerRepository.saveAll_(data);
//            this.producerRequestRepository.saveAll(data);
        }

        _log.info("end CrawDataSchedule at {} with {} record", new Date(), Validator.isNotNull(data) ? data.size() : 0);
    }
}
