package com.hust.common.base.repository.extend;

import com.hust.common.base.constants.CacheConstants;
import com.hust.common.base.model.Customer;
import com.hust.common.base.model.Record;
import com.hust.common.base.model.dto.request.LookupReq;
import com.hust.common.base.model.dto.request.RecordReq;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RecordRepositoryExtend {
    List<Record> search(LookupReq lookupReq);

    Long count(LookupReq lookupReq);

    List<Record> searchCms(RecordReq recordReq, Pageable pageable);

    Long countCms(RecordReq recordReq);

    Map<String, Long> countTypeRecords(LocalDateTime start, LocalDateTime end);

    Map<LocalDate, Long> countRecordByDay(LocalDateTime start, LocalDateTime end);
}
