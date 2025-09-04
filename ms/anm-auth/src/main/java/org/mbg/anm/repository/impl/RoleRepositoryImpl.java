package org.mbg.anm.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.mbg.anm.model.Permission;
import org.mbg.anm.model.Role;
import org.mbg.anm.repository.extend.RoleRepositoryExtend;

import java.util.List;

public class RoleRepositoryImpl implements RoleRepositoryExtend {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Role> findByUserId(Long userId) {
        StringBuilder sql = new StringBuilder(1);
        sql.append("""
                select *
                    from role_ p
                    right join user_role rp on rp.role_code = p.code
                    where rp.user_id = :userId
                """);

        Query query = em.createNativeQuery(sql.toString(), Permission.class);

        query.setParameter("userId", userId);

        return query.getResultList();
    }
}
