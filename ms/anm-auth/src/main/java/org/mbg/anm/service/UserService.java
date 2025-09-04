package org.mbg.anm.service;

import org.mbg.anm.model.dto.UserDTO;

public interface UserService {

    UserDTO getByUsername(String username);

    UserDTO update(UserDTO userDTO);

    UserDTO create(UserDTO userDTO);
}
