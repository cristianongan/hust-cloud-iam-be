package org.mbg.anm.repository.extend;

import org.mbg.anm.constant.AuthConstant;
import org.mbg.anm.model.Permission;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface PermissionRepositoryExtend {
    @Cacheable(cacheNames = AuthConstant.CACHE.ROLE_PERMISSION, key = "#roleCode", unless = "#result == null")
    List<Permission> findByRoleCode(String roleCode);
}
