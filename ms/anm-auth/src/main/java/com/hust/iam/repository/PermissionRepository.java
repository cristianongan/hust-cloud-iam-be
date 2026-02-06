package com.hust.iam.repository;

import com.hust.iam.constant.AuthConstant;
import com.hust.iam.model.Permission;
import com.hust.iam.repository.extend.PermissionRepositoryExtend;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, PermissionRepositoryExtend {

    List<Permission> findAllByCodeIn(List<String> code);

    @Cacheable(cacheNames = AuthConstant.CACHE.PERMISSION_FIND_TYPE_AND_STATUS, unless = "#result == null")
    List<Permission> findAllByTypeAndStatus(Integer type, Integer status);
}
