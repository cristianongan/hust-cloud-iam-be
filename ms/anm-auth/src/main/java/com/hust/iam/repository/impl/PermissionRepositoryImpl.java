package com.hust.iam.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import com.hust.iam.model.Permission;
import com.hust.iam.repository.extend.PermissionRepositoryExtend;

import java.util.List;

public class PermissionRepositoryImpl implements PermissionRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Permission> findByRoleCode(String roleCode) {
        StringBuilder sql = new StringBuilder(1);
        sql.append("""
                select p.*
                    from permission_ p
                    left join role_permission rp on rp.permission_code = p.code
                    where rp.role_code = :roleCode
                """);

        Query query = em.createNativeQuery(sql.toString(), Permission.class);

        query.setParameter("roleCode", roleCode);

        return query.getResultList();
    }
}
