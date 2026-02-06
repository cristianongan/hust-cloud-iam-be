package com.hust.iam.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChangeTotalQuotaReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 8450737088976099873L;

    private Long id;

    private Long quota;

    private Long userId;
}
