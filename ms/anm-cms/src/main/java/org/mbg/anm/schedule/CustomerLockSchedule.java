package org.mbg.anm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.base.schedule.Worker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduling.customer-lock", name = "enabled", havingValue = "true", matchIfMissing = false)
public class CustomerLockSchedule implements Worker {
    private final CustomerRepository customerRepository;

    @Override
    @Scheduled(cron = "${scheduling.customer-lock.cron}")
    @SchedulerLock(name = "${scheduling.customer-lock.name:customer-lock}",
            lockAtLeastFor = "${scheduling.customer-lock.lock-at-least}",
            lockAtMostFor = "${scheduling.customer-lock.lock-at-most}")
    @Async("scheduleExecutor")
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        _log.info("CustomerLockSchedule start run at {}", now);

        this.customerRepository.updateStatusExpiredCustomer(EntityStatus.INACTIVE.getStatus(),  now);

        _log.info("CustomerLockSchedule finish run at {}", LocalDateTime.now());
    }
}
