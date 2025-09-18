package org.mbg.anm.repository.extend;

import org.mbg.anm.model.Role;
import org.mbg.anm.model.dto.request.RoleReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleRepositoryExtend {
    List<Role> findByUserId(Long userId);

    List<Role> findByClientId(String clientId);

    List<Role> search(RoleReq roleReq, Pageable pageable);

    Long count (RoleReq roleReq);
}
