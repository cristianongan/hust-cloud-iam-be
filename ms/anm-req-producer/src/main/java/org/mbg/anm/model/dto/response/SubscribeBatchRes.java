package org.mbg.anm.model.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SubscribeBatchRes implements Serializable {
    @Serial
    private static final long serialVersionUID = -7746170555038411794L;

    private Long createTime;
}
