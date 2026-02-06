package com.hust.iam.model.dto.request;

import lombok.Data;

@Data
public class ClientTokenReq {
    private String clientId;

    private String clientSecret;
}
