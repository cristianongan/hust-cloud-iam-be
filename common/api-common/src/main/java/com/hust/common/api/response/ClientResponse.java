package com.hust.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.hust.common.base.enums.ErrorCode;

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
