package com.hust.common.base.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class IncreaseInUseRes implements Serializable {
    @Serial
    private static final long serialVersionUID = -1195662574344393469L;

    private Long inUse;
}
