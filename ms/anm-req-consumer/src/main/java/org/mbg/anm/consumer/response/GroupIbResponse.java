package org.mbg.anm.consumer.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mbg.common.api.response.Response;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class GroupIbResponse extends Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -675219897230178316L;

    private int code;
    private String message;
}
