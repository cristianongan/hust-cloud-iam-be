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

    LookupResponse lookup(LookupReq lookupReq, String clientId);

    InfoRes info();

    TransactionResponse sendOtpToVerify(CustomerDataReq customerDataReq, String clientId);

    void verify(CustomerDataReq otpReq, String clientId);

    void addDataLookup(SubscribeReq subscribeReq, String clientId);

    void removeDataLookup(SubscribeReq subscribeReq, String clientId);
}
