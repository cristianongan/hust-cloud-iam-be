package com.hust.iam.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRes {
    private String username;

    private Long userId;
}
