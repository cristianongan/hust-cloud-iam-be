package org.mbg.anm.service;

import org.mbg.common.base.model.dto.UserDTO;
import org.mbg.common.base.model.dto.request.UserReq;
import org.mbg.anm.model.search.UserSearch;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO detail();

    UserDTO update(UserReq userReq);

    UserDTO create(UserReq userReq);

    Page<UserDTO> searchUsers(UserSearch search);

    void updateStatus(UserReq userReq);

    void delete(UserReq userReq);

    void assignRole(UserReq userReq);

    void disable(UserReq userReq);

    void enable(UserReq userReq);
}
