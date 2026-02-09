package com.hust.iam.service.mapper;

import org.mapstruct.Mapper;
import com.hust.iam.model.dto.RecordDTO;
import com.hust.common.base.model.Record;
import com.hust.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface RecordMapper extends EntityMapper<RecordDTO, Record> {
}
