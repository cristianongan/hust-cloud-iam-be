package org.mbg.anm.service.mapper;

import org.mapstruct.Mapper;
import org.mbg.anm.model.dto.RecordDTO;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface RecordMapper extends EntityMapper<RecordDTO, Record> {
}
