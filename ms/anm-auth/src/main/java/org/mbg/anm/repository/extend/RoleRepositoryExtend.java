package org.mbg.anm.repository.extend;

import org.mbg.anm.model.Role;

import java.util.List;

public interface RoleRepositoryExtend {
    List<Role> findByUserId(Long userId);

    List<Role> findByClientId(String clientId);
}
