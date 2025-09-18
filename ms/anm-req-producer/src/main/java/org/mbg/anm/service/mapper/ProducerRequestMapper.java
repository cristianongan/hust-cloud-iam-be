package org.mbg.anm.service.mapper;


import org.mapstruct.Mapper;
import org.mbg.common.base.model.ProducerRequest;
import org.mbg.common.base.model.dto.ProducerRequestDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface ProducerRequestMapper extends EntityMapper<ProducerRequestDTO, ProducerRequest> {}
