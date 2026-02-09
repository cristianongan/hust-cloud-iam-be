package com.hust.iam.service.mapper;

import org.mapstruct.*;
import com.hust.iam.model.Role;
import com.hust.iam.model.User;
import com.hust.common.base.model.dto.UserDTO;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.service.mapper.EntityMapper;
import com.hust.common.util.Validator;

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
