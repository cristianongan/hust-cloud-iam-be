package com.hust.iam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.model.Permission;
import com.hust.iam.model.Role;
import com.hust.iam.model.RolePermission;
import com.hust.iam.model.dto.PermissionDTO;
import com.hust.iam.model.dto.RoleDTO;
import com.hust.iam.model.dto.request.RoleReq;
import com.hust.iam.repository.PermissionRepository;
import com.hust.iam.repository.RolePermissionRepository;
import com.hust.iam.repository.RoleRepository;
import com.hust.iam.repository.UserRepository;
import com.hust.iam.service.RoleService;
import com.hust.iam.service.mapper.PermissionMapper;
import com.hust.iam.service.mapper.RoleMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.PermissionType;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final PermissionMapper permissionMapper;

    private final UserRepository userRepository;

    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleDTO createRole(RoleReq roleReq) {
        if (Validator.isNull(roleReq.getName())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INVALID_INPUT_DATA,
                    new String[] {Labels.getLabels(LabelKey.LABEL_ROLE_NAME)}), Role.class.getName(),
                    LabelKey.ERROR_INVALID_INPUT_DATA);
        }

        if (Validator.isNull(roleReq.getCode())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[] {Labels.getLabels(LabelKey.LABEL_RECORD_ID)}), Role.class.getName(),
                    LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        if (this.roleRepository.existsByCodeAndStatusNot(roleReq.getCode(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[] {Labels.getLabels(LabelKey.LABEL_RECORD_ID)}), Role.class.getName(),
                    LabelKey.ERROR_DUPLICATE_DATA);
        }

        Role role = new Role();

        role.setStatus(EntityStatus.ACTIVE.getStatus());
        role.setCode(roleReq.getCode());
        role.setName(roleReq.getName());
        role.setDescription(roleReq.getDescription());

        this.savePermission(roleReq);

        return this.roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO updateRole(RoleReq roleReq) {
        if (Validator.isNull(roleReq.getName())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INVALID_INPUT_DATA,
                    new String[] {Labels.getLabels(LabelKey.LABEL_ROLE_NAME)}), Role.class.getName(),
                    LabelKey.ERROR_INVALID_INPUT_DATA);
        }

        Role role = this.roleRepository.findByCodeAndStatusNot(roleReq.getCode(), EntityStatus.DELETED.getStatus());

        if (Validator.isNull(role)) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[] {Labels.getLabels(LabelKey.LABEL_ROLE_NAME)}), Role.class.getName(),
                    LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        role.setName(roleReq.getName());
        role.setDescription(roleReq.getDescription());


        this.rolePermissionRepository.removeAllPermissionByCode(role.getCode());

        this.savePermission(roleReq);

        return this.roleMapper.toDto(roleRepository.save(role));
    }

    private void savePermission(RoleReq roleReq) {
        List<RolePermission> rolePermissions = new ArrayList<>();

        if (Validator.isNotNull(roleReq.getPermissions())) {
            List<Permission> permissions = this.permissionRepository.findAllByCodeIn(roleReq.getPermissions());

            if (Validator.isNotNull(permissions)) {
                for (Permission permission : permissions) {
                    RolePermission rolePermission = new  RolePermission();

                    rolePermission.setRoleCode(roleReq.getCode());
                    rolePermission.setPermissionCode(permission.getCode());

                    rolePermissions.add(rolePermission);
                }
                this.rolePermissionRepository.saveAll(rolePermissions);
            }
        }
    }

    @Override
    @Transactional
    public void deleteRole(RoleReq roleReq) {
        if (Validator.isNotNull(roleReq.getIds())) {
            this.roleRepository.updateStatusByIdIn(EntityStatus.DELETED.getStatus(), roleReq.getIds());
        }
    }

    @Override
    public Page<RoleDTO> search(RoleReq search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getPageSize());

        List<Role> roles = this.roleRepository.search(search, pageable);

        roles.forEach(role -> {
            if (Validator.equals(role.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                List<Permission> pves = this.permissionRepository.findByRoleCode(role.getCode());

                role.setPermissions(pves);
            }
        });

        List<RoleDTO> content = this.roleMapper.toDto(roles);

        Long count  = this.roleRepository.count(search);

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<PermissionDTO> getAllPermission() {
        return this.permissionMapper.toDto(this.permissionRepository.findAllByTypeAndStatus(PermissionType.USER.getValue(), EntityStatus.ACTIVE.getStatus()));
    }

    @Override
    public List<RoleDTO> getAllRole() {
        return this.roleMapper.toDto(this.roleRepository.findAllByStatus(EntityStatus.ACTIVE.getStatus()));
    }
}
