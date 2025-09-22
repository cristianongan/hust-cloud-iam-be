package org.mbg.anm.consumer.request;

import lombok.Data;
import org.mbg.common.api.request.Request;

import java.io.Serial;

@Data
public class GroupIbReq extends Request {
    @Serial
    private static final long serialVersionUID = 8755207988667303460L;

    private String q;

    private int offset = 0;

    private int limit = 100;
}
