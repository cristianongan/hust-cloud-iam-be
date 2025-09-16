package org.mbg.common.base.model.dto;

import lombok.Data;

@Data
public class ProducerRequestDTO {
    private String requestId;

    private String email;

    private String phone;

    private String identification;

    private String clientId;

    private int status;
}
