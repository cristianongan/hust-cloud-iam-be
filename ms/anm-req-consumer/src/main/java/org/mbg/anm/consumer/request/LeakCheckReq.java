package org.mbg.anm.consumer.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mbg.common.api.request.Request;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class LeakCheckReq extends Request implements Serializable {
    @Serial
    private static final long serialVersionUID = -5203407997364949254L;

    private String check;
}
