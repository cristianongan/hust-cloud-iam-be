package org.mbg.common.base.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.common.base.model.dto.request.RecordReq;
import org.mbg.common.base.repository.extend.RecordRepositoryExtend;
import org.mbg.common.util.TimeUtil;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordRepositoryImpl implements RecordRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<org.mbg.common.base.model.Record> search(LookupReq lookupReq) {
        StringBuilder sql = new StringBuilder("from Record e left join fetch e.types ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(lookupReq,params));

        Query query = em.createQuery(sql.toString(), org.mbg.common.base.model.Record.class);

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

        Query query = em.createQuery(sql.toString(), org.mbg.common.base.model.Record.class);

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
        params.put("deletedStatus", EntityStatus.DELETED.getStatus());

        if (Validator.isNotNull(lookupReq.getSubscriberId())) {
            sql.append(" AND e.customerId = :customerId ");
            params.put("customerId",lookupReq.getCustomerId());
        }

        return sql;
    }
}
