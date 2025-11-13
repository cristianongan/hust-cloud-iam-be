package org.mbg.common.base.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.mbg.common.base.repository.extend.CustomerLogRepositoryExtend;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerLogRepositoryImpl implements CustomerLogRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public Map<LocalDate, Long> countRecordByDay(LocalDateTime start, LocalDateTime end) {
        String sql = "select (date_trunc('day', e.created_date AT TIME ZONE 'Asia/Ho_Chi_Minh'))::date AS day_vn, count(1) " +
                "from customer_log e "
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
}
