package org.mbg.anm.service;

import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.anm.model.dto.request.RoleReq;

public interface RoleService {
    RoleDTO createRole(RoleReq roleReq);

    RoleDTO updateRole(RoleReq roleReq);

    void deleteRole(RoleReq roleReq);
}
