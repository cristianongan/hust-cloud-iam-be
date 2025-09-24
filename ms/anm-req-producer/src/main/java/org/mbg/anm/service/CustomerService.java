package org.mbg.anm.service;

import org.mbg.anm.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.model.dto.response.LookupResponse;
import org.mbg.anm.model.dto.response.SubscribeRes;

public interface CustomerService {
    SubscribeRes subscribe(SubscribeReq subscribeReq);

    SubscribeRes unSubscribe(SubscribeReq subscribeReq);

    LookupResponse lookup(LookupReq lookupReq);
}
