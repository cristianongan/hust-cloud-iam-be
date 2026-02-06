package com.hust.common.base.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommonDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5935540107045404564L;

    private Long id;

    private String type;

    private String code;

    private String value;

    private Integer status;
}
