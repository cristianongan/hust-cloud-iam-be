package org.mbg.anm.model.dto.request;

import lombok.Data;

@Data
public class ClientTokenReq {
    private String clientId;

    private String clientSecret;
}
