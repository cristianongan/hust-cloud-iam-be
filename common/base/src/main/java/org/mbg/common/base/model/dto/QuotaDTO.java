package org.mbg.common.base.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QuotaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6065233791097334179L;

    private Long id;

    private Long userId;

    private Long inUse;

    private Long total;
}
