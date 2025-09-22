package org.mbg.anm.repository;

import org.mbg.anm.model.ClientRole;
import org.mbg.anm.model.RolePermission;
import org.mbg.anm.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(nativeQuery = true, value = "delete from user_role where user_id = :id")
    @Modifying
    Integer removeAllRoleByUserId(Long id);

    @Query(nativeQuery = true, value = """
    select e.* from user_role e 
        inner join role r on r.code = e.role_code
    where e.user_id in :ids and r.status = :roleStatus
""")
    List<UserRole> findByUserIdIn(List<Long> ids, int roleStatus);
}
