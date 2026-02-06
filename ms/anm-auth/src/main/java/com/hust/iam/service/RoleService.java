package com.hust.iam.service;

import com.hust.iam.model.dto.PermissionDTO;
import com.hust.iam.model.dto.RoleDTO;
import com.hust.iam.model.dto.request.RoleReq;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleReq roleReq);

    RoleDTO updateRole(RoleReq roleReq);

    void deleteRole(RoleReq roleReq);

    Page<RoleDTO> search(RoleReq roleReq);

    List<PermissionDTO> getAllPermission();

    List<RoleDTO> getAllRole();
}
