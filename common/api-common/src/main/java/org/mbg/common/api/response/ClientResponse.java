package org.mbg.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mbg.common.api.enums.ClientResponseError;
import org.mbg.common.base.enums.ErrorCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ClientResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -9183675082950983744L;

    private String reasonCode;

    private String message;

    private Object result;

    public static ClientResponse ok(Object data) {
        return new ClientResponse(ErrorCode.MSG0000.name(), "", data);
    }
}
