package org.mbg.anm.model.dto.request;

import lombok.Data;

@Data
public class CustomerDataReq {
    private String type;

    private String value;

    private Integer status;

    private String customerKey;

    private String transactionId;

    private String otp;
}
