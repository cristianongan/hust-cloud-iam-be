package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.UserDTO;

public interface UserService {

    UserDTO update(UserDTO userDTO);

    UserDTO create(UserDTO userDTO);

    JwtAccessToken login(UserDTO userDTO);
}
