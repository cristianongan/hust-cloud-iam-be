package org.mbg.anm.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mbg.anm.model.Permission;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

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
        return role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toList());
    }
}
