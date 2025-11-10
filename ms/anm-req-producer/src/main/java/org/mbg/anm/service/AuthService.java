package org.mbg.anm.service;

import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.common.base.model.JwtAccessToken;

public interface AuthService {
    JwtAccessToken verifyCustomer(SubscribeReq req, String org);
}
