package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.UserDTO;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.anm.model.dto.request.UserReq;
import org.mbg.anm.model.search.UserSearch;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDTO update(UserReq userReq);

    UserDTO create(UserReq userReq);

    Page<UserDTO> searchUsers(UserSearch search);

    void updateStatus(UserReq userReq);

    void delete(UserReq userReq);

    void assignRole(UserReq userReq);
}
