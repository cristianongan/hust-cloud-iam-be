package com.hust.iam.repository.extend;

import com.hust.iam.model.Permission;

import java.util.List;

public interface PermissionRepositoryExtend {
//    @Cacheable(cacheNames = AuthConstant.CACHE.ROLE_PERMISSION, key = "#roleCode", unless = "#result == null")
    List<Permission> findByRoleCode(String roleCode);
}
