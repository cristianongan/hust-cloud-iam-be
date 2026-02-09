package com.hust.common.base.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import com.hust.common.base.enums.CustomerSyncStatus;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.model.CustomerData;
import com.hust.common.base.repository.extend.CustomerDataRepositoryExtend;

import java.util.List;

public class CustomerDataRepositoryImpl implements CustomerDataRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<CustomerData> getLookupData(CustomerSyncStatus customerSyncStatus, EntityStatus status, Integer limit) {
        StringBuilder sql = new StringBuilder("""
                select e.* from customer_data e
                inner join customer c on e.customer_key = c.customer_key and c.status = :status
                where e.sync_status = :syncStatus and e.verify = 1
                order by e.last_scan NULLS FIRST
                """);

        Query query = em.createNativeQuery(sql.toString(), CustomerData.class);
        query.setParameter("status", status.getStatus());
        query.setParameter("syncStatus", customerSyncStatus.getStatus());
        query.setFirstResult(0);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
