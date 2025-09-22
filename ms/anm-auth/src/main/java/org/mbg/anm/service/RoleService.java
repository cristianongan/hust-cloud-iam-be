package org.mbg.anm.service;

import org.mbg.anm.model.Permission;
import org.mbg.anm.model.dto.PermissionDTO;
import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.anm.model.dto.request.RoleReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleReq roleReq);

    RoleDTO updateRole(RoleReq roleReq);

    void deleteRole(RoleReq roleReq);

    Page<RoleDTO> search(RoleReq roleReq);

    List<PermissionDTO> getAllPermission();

    List<RoleDTO> getAllRole();
}
