package org.mbg.anm.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class SubscribeBatchReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -5860961771147126755L;

    private String reference;

    private List<DataReq> dataReqs;

    @Data
    public static class DataReq {
        private String subscriberId;

        private String type;

        private Long startTime;

        private Long endTime;
    }
}
