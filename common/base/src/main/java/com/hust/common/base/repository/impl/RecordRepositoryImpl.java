package com.hust.common.base.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.model.Record;
import com.hust.common.base.model.dto.request.LookupReq;
import com.hust.common.base.model.dto.request.RecordReq;
import com.hust.common.base.repository.extend.RecordRepositoryExtend;
import com.hust.common.util.TimeUtil;
import com.hust.common.util.Validator;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordRepositoryImpl implements RecordRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<com.hust.common.base.model.Record> search(LookupReq lookupReq) {
        StringBuilder sql = new StringBuilder("from Record e left join fetch e.types ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(lookupReq,params));

        Query query = em.createQuery(sql.toString(), com.hust.common.base.model.Record.class);

        params.forEach(query::setParameter);

        return query.getResultList();
    }

    @Override
    public Long count(LookupReq lookupReq) {
        StringBuilder sql = new StringBuilder("select count(1) from Record e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(lookupReq,params));

        Query query = em.createQuery(sql.toString(), Long.class);

        params.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Record> searchCms(RecordReq recordReq, Pageable pageable) {
        StringBuilder sql = new StringBuilder("from Record e left join fetch e.types ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQueryCms(recordReq,params));

        Query query = em.createQuery(sql.toString(), com.hust.common.base.model.Record.class);

        params.forEach(query::setParameter);

        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

            query.setMaxResults(pageable.getPageSize());
        } else {
            query.setFirstResult(0);

            query.setMaxResults(10);
        }

        return query.getResultList();
    }

    @Override
    public Long countCms(RecordReq recordReq) {
        StringBuilder sql = new StringBuilder("select count(1) from Record e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQueryCms(recordReq,params));

        Query query = em.createQuery(sql.toString(), Long.class);

        params.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public Map<String, Long> countTypeRecords(LocalDateTime start, LocalDateTime end) {
        String sql = "select type, count(1) as count from record_type e inner join record_ cd " + " on cd.id = e.record_id " +
                " where cd.created_date >= :start and cd.created_date <= :end " +
                " group by e.type ";

        Map<String,Object> params = new HashMap<>();

        params.put("start", start);
        params.put("end", end);

        Query query = em.createNativeQuery(sql);

        params.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream().collect(
                Collectors.toMap(
                        r -> (String) r[0],
                        r -> ((Number) r[1]).longValue(),
                        Long::sum,
                        LinkedHashMap::new
                )
        );
    }

    @Override
    public Map<LocalDate, Long> countRecordByDay(LocalDateTime start, LocalDateTime end) {
        String sql = "select (date_trunc('day', e.created_date AT TIME ZONE 'Asia/Ho_Chi_Minh'))::date AS day_vn, count(1) from record_ e "
                + " where e.created_date >= :start and e.created_date <= :end "
                + " GROUP BY day_vn";

        Map<String,Object> params = new HashMap<>();

        params.put("start", start);
        params.put("end", end);

        Query query = em.createNativeQuery(sql);

        params.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream().collect(
                Collectors.toMap(
                        r -> ((Date) r[0]).toLocalDate(),
                        r -> ((Number) r[1]).longValue(),
                        Long::sum,
                        LinkedHashMap::new
                )
        );
    }

    private StringBuilder createWhereQueryCms(RecordReq recordReq, Map<String, Object> params) {

        StringBuilder sql = new StringBuilder(" WHERE 1=1 AND e.status != :deletedStatus ");
        params.put("deletedStatus", EntityStatus.DELETED.getStatus());

        sql.append(" AND e.detectTime >= :detectTimeStart AND e.detectTime <= :detectTimeEnd ");
        params.put("detectTimeStart", recordReq.getDetectTimeStart().atStartOfDay(TimeUtil.VN_TIME_ZONE).toEpochSecond());
        params.put("detectTimeEnd", recordReq.getDetectTimeEnd().atTime(LocalTime.MAX).atZone(TimeUtil.VN_TIME_ZONE).toEpochSecond());

        if (Validator.isNotNull(recordReq.getLeakId())) {
            sql.append(" AND e.leakId = :leakId ");
            params.put("leakId", recordReq.getLeakId());
        }

        if (Validator.isNotNull(recordReq.getDataSource())) {
            sql.append(" AND e.dataSource = :dataSource ");
            params.put("dataSource", recordReq.getDataSource());
        }

        return sql;
    }

    private StringBuilder createWhereQuery(LookupReq lookupReq, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1 AND e.status != :deletedStatus ");
        sql.append(" AND exists (select 1 from CustomerData c where c.customerKey = e.customerKey and c.value = e.dataLookup and c.status = :activeStatus) ");
        params.put("deletedStatus", EntityStatus.DELETED.getStatus());
        params.put("activeStatus", EntityStatus.ACTIVE.getStatus());

        if (Validator.isNotNull(lookupReq.getCustomerKey())) {
            sql.append(" AND e.customerKey = :customerKey ");
            params.put("customerKey",lookupReq.getCustomerKey());
        }

        return sql;
    }
}
