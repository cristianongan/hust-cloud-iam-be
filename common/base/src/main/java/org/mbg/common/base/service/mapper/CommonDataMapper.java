package org.mbg.common.base.service.mapper;

import org.mapstruct.Mapper;
import org.mbg.common.base.model.CommonData;
import org.mbg.common.base.model.dto.CommonDataDTO;

@Mapper(componentModel = "spring")
public interface CommonDataMapper extends EntityMapper<CommonDataDTO, CommonData> {
}
