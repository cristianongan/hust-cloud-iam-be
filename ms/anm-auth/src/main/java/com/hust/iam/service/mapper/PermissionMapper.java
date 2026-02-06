package com.hust.iam.service.mapper;

import org.mapstruct.Mapper;
import com.hust.iam.model.Permission;
import com.hust.iam.model.dto.PermissionDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
}
