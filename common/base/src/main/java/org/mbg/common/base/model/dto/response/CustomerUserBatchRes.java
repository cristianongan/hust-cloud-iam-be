package org.mbg.common.base.model.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CustomerUserBatchRes implements Serializable {
    @Serial
    private static final long serialVersionUID = 5428611955299124523L;

    private List<CustomerUserRes> customerUserRes;
}
