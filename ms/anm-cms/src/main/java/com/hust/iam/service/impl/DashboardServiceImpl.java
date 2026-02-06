package com.hust.iam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.model.dto.response.DashboardRes;
import com.hust.iam.service.DashboardService;
import org.mbg.common.base.constants.CacheConstants;
import org.mbg.common.base.repository.CustomerLogRepository;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.util.Validator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final RecordRepository recordRepository;

    private final CustomerLogRepository customerLogRepository;

    @Override
    @Cacheable(cacheNames = CacheConstants.DASHBOARD.DASHBOARD, unless = "#result == null")
    public DashboardRes get() {
        return DashboardRes.builder()
                .typeRates(this.getRecordTypeRate())
                .recordCounts(this.getRecordCount())
                .subscribeCounts(this.getSubscribeCount())
                .build();
    }

    public List<DashboardRes.Count> getRecordCount() {
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

        Map<LocalDate, Long> res = this.recordRepository.countRecordByDay(now.minusMonths(1L), now);
        List<DashboardRes.Count> content = new ArrayList<>();

        if (Validator.isNotNull(res)) {
            for(Map.Entry<LocalDate, Long> entry : res.entrySet()) {
                content.add(DashboardRes.Count.builder()
                                .time(entry.getKey())
                                .count(entry.getValue())
                        .build());
            }
        }

        return content;
    }

    public List<DashboardRes.Count> getSubscribeCount() {
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

        Map<LocalDate, Long> res = this.customerLogRepository.countRecordByDay(now.minusMonths(1L), now);
        List<DashboardRes.Count> content = new ArrayList<>();

        if (Validator.isNotNull(res)) {
            for(Map.Entry<LocalDate, Long> entry : res.entrySet()) {
                content.add(DashboardRes.Count.builder()
                        .time(entry.getKey())
                        .count(entry.getValue())
                        .build());
            }
        }

        return content;
    }



    public List<DashboardRes.Rate> getRecordTypeRate() {
        LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

        Map<String, Long> res = this.recordRepository.countTypeRecords(now.minusYears(1L), now);
        List<DashboardRes.Rate> rates = new ArrayList<>();

        if (Validator.isNotNull(res)) {
            long total = 0L;
            double cur = 0d;
            for (Long item : res.values()) {
                if (Validator.isNotNull(item)) {
                    total += item;
                }
            }

            for (Map.Entry<String, Long> entry : res.entrySet()) {
                if (Validator.isNotNull(entry.getValue())) {
                    double valueRate = entry.getValue()*100.0/total;
                    DashboardRes.Rate rate = DashboardRes.Rate.builder()
                            .value(entry.getKey())
                            .rate(valueRate)
                            .build();
                    rates.add(rate);
                    cur += valueRate;
                }

                if (cur > 95) {
                    rates.add(DashboardRes.Rate.builder()
                            .value("another")
                            .rate(100.0-cur)
                            .build());
                    break;
                }
            }
        }

        return rates;
    }
}
