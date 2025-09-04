package org.mbg.anm.repository.extend;

import org.mbg.anm.model.Permission;

import java.util.List;

public interface PermissionRepositoryExtend {
    List<Permission> findByRoleCode(String roleCode);
}
