package org.mbg.anm.repository;

import org.mbg.anm.model.Permission;
import org.mbg.anm.repository.extend.PermissionRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, PermissionRepositoryExtend {
}
