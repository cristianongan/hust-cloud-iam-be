package com.hust.iam.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import com.hust.iam.model.Client;
import com.hust.iam.model.dto.request.ClientReq;
import com.hust.iam.repository.extend.ClientRepositoryExtend;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRepositoryImpl implements ClientRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Client> search(ClientReq clientReq, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select e.* from client_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(clientReq,params));
        sql.append(" ORDER BY e.LAST_MODIFIED_DATE desc ");

        Query query = em.createNativeQuery(sql.toString(), Client.class);

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
    public Long count(ClientReq clientReq) {
        StringBuilder sql = new StringBuilder("select count(1) from client_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(clientReq,params));

        Query query = em.createNativeQuery(sql.toString(), Long.class);

        params.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    private StringBuilder createWhereQuery(ClientReq clientReq, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1");
        sql.append(" AND e.status != :deleteStatus ");
        params.put("deleteStatus", EntityStatus.DELETED.getStatus());

        if (Validator.isNotNull(clientReq.getUsername())) {
            sql.append(" AND EXISTS (SELECT 1 from user_ u where u.id = e.user_id and u.username = :username) ");
            params.put("username", clientReq.getUsername());
        }

        return sql;
    }
}
