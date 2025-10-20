package org.mbg.common.base.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ResetPasswordReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -975904513789139955L;

    private String phone;

    private String email;
}
