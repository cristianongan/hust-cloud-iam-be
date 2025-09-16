package org.mbg.anm.repository;

import org.mbg.anm.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    @Query(nativeQuery = true, value = "delete from role_permission where role_code = :code")
    @Modifying
    Integer removeAllPermissionByCode(String code);
}
