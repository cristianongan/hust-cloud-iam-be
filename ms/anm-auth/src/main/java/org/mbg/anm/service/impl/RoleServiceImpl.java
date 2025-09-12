package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.User;
import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.anm.model.dto.UserDTO;
import org.mbg.anm.model.dto.request.RoleReq;
import org.mbg.anm.repository.PermissionRepository;
import org.mbg.anm.repository.RoleRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.anm.service.RoleService;
import org.mbg.anm.service.mapper.RoleMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.RandomGenerator;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final UserRepository userRepository;

    private final RoleMapper roleMapper;

    @Override
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

        return this.roleMapper.toDto(roleRepository.save(role));
    }

    @Override
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

        return this.roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public void deleteRole(RoleReq roleReq) {
        if (Validator.isNotNull(roleReq.getIds())) {
            this.roleRepository.updateStatusByIdIn(EntityStatus.DELETED.getStatus(), roleReq.getIds());
        }
    }

    @Override
    public Page<RoleDTO> search(RoleReq search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getPageSize());

        List<Role> roles = this.roleRepository.search(search, pageable);

        List<RoleDTO> content = this.roleMapper.toDto(roles);

        Long count  = this.roleRepository.count(search);

        return new PageImpl<>(content, pageable, count);
    }
}
