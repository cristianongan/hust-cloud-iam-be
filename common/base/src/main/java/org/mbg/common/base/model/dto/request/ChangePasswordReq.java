package org.mbg.common.base.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChangePasswordReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 6362704127997750839L;

    private String oldPassword;

    private String newPassword;
}
