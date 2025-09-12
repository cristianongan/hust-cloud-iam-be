package org.mbg.anm.service;

import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.anm.model.dto.request.RoleReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleDTO createRole(RoleReq roleReq);

    RoleDTO updateRole(RoleReq roleReq);

    void deleteRole(RoleReq roleReq);

    Page<RoleDTO> search(RoleReq roleReq);
}
