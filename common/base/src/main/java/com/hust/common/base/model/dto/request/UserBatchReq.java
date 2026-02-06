package com.hust.common.base.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBatchReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1099015195945870303L;

    private List<UserReq> users;
}
