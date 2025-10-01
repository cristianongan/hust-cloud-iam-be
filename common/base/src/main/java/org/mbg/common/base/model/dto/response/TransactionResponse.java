package org.mbg.common.base.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class TransactionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -2526810452973815972L;

    private String transactionId;
}
