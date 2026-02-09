package com.hust.iam.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import com.hust.iam.model.Role;
import com.hust.iam.model.dto.request.RoleReq;
import com.hust.iam.repository.extend.RoleRepositoryExtend;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.util.Validator;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleRepositoryImpl implements RoleRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Role> findByUserId(Long userId) {
        StringBuilder sql = new StringBuilder(1);
        sql.append("""
                select p.*
                    from role_ p
                    right join user_role rp on rp.role_code = p.code
                    where rp.user_id = :userId
                """);

        Query query = em.createNativeQuery(sql.toString(), Role.class);

        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public List<Role> findByClientId(String clientId) {
        StringBuilder sql = new StringBuilder(1);
        sql.append("""
                select p.*
                    from role_ p
                    right join client_role rp on rp.role_code = p.code
                    where rp.client_id = :clientId
                """);

        Query query = em.createNativeQuery(sql.toString(), Role.class);

        query.setParameter("clientId", clientId);

        return query.getResultList();
    }

    @Override
    public List<Role> search(RoleReq roleReq, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select e.* from role_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(roleReq,params));

        Query query = em.createNativeQuery(sql.toString(), Role.class);

        sql.append(" order by e.last_modified_date desc nulls last ");

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
    public Long count(RoleReq roleReq) {
        StringBuilder sql = new StringBuilder("select count(1) from role_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(roleReq,params));

        Query query = em.createNativeQuery(sql.toString(), Long.class);

        params.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    private StringBuilder createWhereQuery(RoleReq roleReq, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1 AND e.status != :deletedStatus ");
        params.put("deletedStatus", EntityStatus.DELETED.getStatus());

        if (Validator.isNotNull(roleReq.getKeyword())) {
            sql.append(" AND (e.name LIKE :keyword OR e.code LIKE :keyword) ");
            params.put("keyword", String.format("%%%s%%", roleReq.getKeyword()));
        }

        return sql;
    }
}
