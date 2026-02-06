package com.hust.common.base.repository.extend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface CustomerLogRepositoryExtend {
    Map<LocalDate, Long> countRecordByDay(LocalDateTime start, LocalDateTime end);
}
