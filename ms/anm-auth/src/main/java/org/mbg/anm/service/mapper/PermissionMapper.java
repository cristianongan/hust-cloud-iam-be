package org.mbg.anm.service.mapper;

import org.mapstruct.Mapper;
import org.mbg.anm.model.Permission;
import org.mbg.anm.model.dto.PermissionDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
}
