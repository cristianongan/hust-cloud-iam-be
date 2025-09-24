package org.mbg.anm.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SubscribeReq {
    private String subscriberId;

    private List<CustomerDataReq> dataReqs;
}
