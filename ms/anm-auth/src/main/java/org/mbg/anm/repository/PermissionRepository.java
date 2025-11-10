package org.mbg.anm.repository;

import org.mbg.anm.constant.AuthConstant;
import org.mbg.anm.model.Permission;
import org.mbg.anm.repository.extend.PermissionRepositoryExtend;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, PermissionRepositoryExtend {

    List<Permission> findAllByCodeIn(List<String> code);

    @Cacheable(cacheNames = AuthConstant.CACHE.PERMISSION_FIND_TYPE_AND_STATUS, unless = "#result == null")
    List<Permission> findAllByTypeAndStatus(Integer type, Integer status);
}
