package com.hust.iam.service.mapper;

import org.mapstruct.Mapper;
import com.hust.common.base.model.Quota;
import com.hust.common.base.model.dto.QuotaDTO;
import com.hust.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface QuotaMapper extends EntityMapper<QuotaDTO, Quota> {
}
