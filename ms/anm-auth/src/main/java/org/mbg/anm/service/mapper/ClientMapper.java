package org.mbg.anm.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mbg.anm.model.Client;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.common.base.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    @Override
    @Mappings({
            @Mapping(source = "entity", target = "username", ignore = true)
    })
    ClientDTO toDto(Client entity);
}
