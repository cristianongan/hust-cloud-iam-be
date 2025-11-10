package org.mbg.anm.consumer.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LeakCheckReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -5203407997364949254L;

    private String check;
}
