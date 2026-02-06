package com.hust.common.base.service.mapper;

import org.mapstruct.Mapper;
import com.hust.common.base.model.CommonData;
import com.hust.common.base.model.dto.CommonDataDTO;

@Mapper(componentModel = "spring")
public interface CommonDataMapper extends EntityMapper<CommonDataDTO, CommonData> {
}
