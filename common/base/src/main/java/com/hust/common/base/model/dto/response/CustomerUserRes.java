package com.hust.common.base.model.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CustomerUserRes implements Serializable {
    @Serial
    private static final long serialVersionUID = 3136584361389595008L;

    private String username;

    private Long userId;
}
