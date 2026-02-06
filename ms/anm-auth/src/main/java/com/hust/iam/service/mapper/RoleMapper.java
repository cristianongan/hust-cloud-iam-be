package com.hust.iam.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.hust.iam.model.Permission;
import com.hust.iam.model.Role;
import com.hust.iam.model.dto.RoleDTO;
import org.mbg.common.base.service.mapper.EntityMapper;
import org.mbg.common.util.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Override
    @Mapping(source = "dto", target = "permissions", ignore = true)
    Role toEntity(RoleDTO dto);

    @Override
    @Mapping(source = "role", target = "permissions", qualifiedByName = "toPermission")
    RoleDTO toDto(Role role);

    @Named("toPermission")
    default List<String> toPermission(Role role) {
        return Validator.isNotNull(role.getPermissions()) ? role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toList()) : List.of();
    }
}
