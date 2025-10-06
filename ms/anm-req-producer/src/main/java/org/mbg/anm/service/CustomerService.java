package org.mbg.anm.service;

import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.anm.model.dto.response.InfoRes;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.model.dto.response.LookupResponse;
import org.mbg.anm.model.dto.response.SubscribeRes;
import org.mbg.common.base.model.dto.request.OtpReq;
import org.mbg.common.base.model.dto.response.TransactionResponse;

public interface CustomerService {
    SubscribeRes subscribe(SubscribeReq subscribeReq);

    SubscribeRes unSubscribe(SubscribeReq subscribeReq);

    LookupResponse lookup(LookupReq lookupReq);

    InfoRes info(SubscribeReq subscribeReq);

    TransactionResponse sendOtpToVerify(CustomerDataReq customerDataReq);

    void verify(CustomerDataReq otpReq);

    void addDataLookup(SubscribeReq subscribeReq);

    void removeDataLookup(SubscribeReq subscribeReq);
}
