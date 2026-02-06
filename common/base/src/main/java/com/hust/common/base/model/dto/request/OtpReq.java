package com.hust.common.base.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OtpReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -8046059893024855939L;

    private String otp;

    private String transactionId;
}
