package org.mbg.common.base.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LookupReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -6361858650285180690L;

    private String subscriberId;

    private Long customerId;
}
