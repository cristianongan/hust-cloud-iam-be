package org.mbg.anm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.base.schedule.Worker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduling.customer-lock", name = "enabled", havingValue = "true", matchIfMissing = false)
public class CustomerLockSchedule implements Worker {
    private final CustomerRepository customerRepository;

    @Override
    @Scheduled(cron = "${scheduling.customer-lock.cron}")
    @SchedulerLock(name = "${scheduling.customer-lock.name:craw-data}",
            lockAtLeastFor = "${scheduling.customer-lock.lock-at-least}",
            lockAtMostFor = "${scheduling.customer-lock.lock-at-most}")
    @Async("scheduleExecutor")
    public void run() {

    }
}
