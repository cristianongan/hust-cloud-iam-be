package org.mbg.common.base.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class QuotaBatchReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1861294996059775704L;

    private List<Long> userIds;
}
