package org.mbg.anm.repository;

import org.mbg.anm.model.Permission;
import org.mbg.anm.repository.extend.PermissionRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, PermissionRepositoryExtend {
    List<Permission> findAllByCodeIn(List<String> code);

    List<Permission> findAllByTypeAndStatus(Integer type, Integer status);
}
