package org.mbg.common.base.model.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class VerifyRes implements Serializable {
    @Serial
    private static final long serialVersionUID = 5889379514454855557L;

    private List<String> permissions;

    private String user;
}
