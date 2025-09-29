package org.mbg.anm.service.mapper;

import org.mapstruct.*;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.User;
import org.mbg.anm.model.dto.UserDTO;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.service.mapper.EntityMapper;
import org.mbg.common.util.Validator;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Override
    @Mappings({
            @Mapping(source = "dto", target = "roles", ignore = true)
    })
    User toEntity(UserDTO dto);

    @Override
    @Mappings({
            @Mapping(source = "user", target = "roles", ignore = true),
            @Mapping(source = "user", target = "quota", ignore = true)
    })
    UserDTO toDto(User user);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTO dto, User entity) {
        List<String> roleDTOs = new ArrayList<>();

        List<Role> roles = entity.getRoles();

        roles.forEach(role -> {
            if (Validator.equals(role.getStatus(), EntityStatus.ACTIVE.getStatus())) {

                roleDTOs.add(role.getCode());
            }
        });

        dto.setRoles(roleDTOs);
    }
}
