package org.mbg.anm.model.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String requestId;

    private String email;

    private String phone;

    private String identification;
}
