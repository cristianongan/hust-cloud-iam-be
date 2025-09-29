package org.mbg.anm.service.mapper;

import org.mapstruct.Mapper;
import org.mbg.common.base.model.Quota;
import org.mbg.common.base.model.dto.QuotaDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface QuotaMapper extends EntityMapper<QuotaDTO, Quota> {
}
