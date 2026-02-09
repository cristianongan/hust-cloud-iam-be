package com.hust.iam.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.hust.iam.model.Client;
import com.hust.iam.model.dto.ClientDTO;
import com.hust.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    @Override
    @Mappings({
            @Mapping(source = "entity", target = "username", ignore = true)
    })
    ClientDTO toDto(Client entity);
}
