package com.hust.iam.service;

import com.hust.common.base.model.dto.UserDTO;
import com.hust.common.base.model.dto.request.UserBatchReq;
import com.hust.common.base.model.dto.request.UserReq;
import com.hust.iam.model.search.UserSearch;
import com.hust.common.base.model.dto.response.CustomerUserBatchRes;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO detail();

    UserDTO update(UserReq userReq);

    UserDTO create(UserReq userReq);

    UserDTO register(UserReq userReq);

    CustomerUserBatchRes customerCreateBatch(UserBatchReq userReq);

    Page<UserDTO> searchUsers(UserSearch search);

    void updateStatus(UserReq userReq);

    void delete(UserReq userReq);

    void assignRole(UserReq userReq);

    void disable(UserReq userReq);

    void enable(UserReq userReq);
}
