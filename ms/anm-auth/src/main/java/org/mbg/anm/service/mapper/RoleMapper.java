package org.mbg.anm.service.mapper;

import org.mapstruct.Mapper;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.dto.RoleDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
}
