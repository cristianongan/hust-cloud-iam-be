package org.mbg.anm.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.mbg.anm.model.User;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.repository.extend.UserRepositoryExtend;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<User> search(UserSearch search, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select e.* from user_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(search,params));

        Query query = em.createNativeQuery(sql.toString(), User.class);

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
    public Long count(UserSearch search) {
        StringBuilder sql = new StringBuilder("select count(1) from user_ e ");

        Map<String,Object> params = new HashMap<>();

        sql.append(createWhereQuery(search,params));

        Query query = em.createNativeQuery(sql.toString(), Long.class);

        params.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    private StringBuilder createWhereQuery(UserSearch search, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append(" WHERE 1 = 1 AND e.status != :deletedStatus ");
        params.put("deletedStatus", EntityStatus.DELETED.getStatus());

        if (Validator.isNotNull(search.getUsername())) {
            sql.append(" AND e.username = :username ");
            params.put("username", search.getUsername());
        }

        if (Validator.isNotNull(search.getEmail())) {
            sql.append(" AND e.email = :email ");
            params.put("email", search.getEmail());
        }

        if (Validator.isNotNull(search.getPhone())) {
            sql.append(" AND e.phone = :phone ");
            params.put("phone", search.getPhone());
        }

        if (Validator.isNotNull(search.getStatuses())) {
            sql.append(" AND e.status IN :statuses ");
            params.put("statuses", search.getStatuses());
        }

        return sql;
    }

}
