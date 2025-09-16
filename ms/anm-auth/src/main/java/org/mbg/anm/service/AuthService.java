package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.common.base.model.dto.response.VerifyRes;

public interface AuthService {
    JwtAccessToken login(LoginReq userDTO);

    JwtAccessToken refreshToken();

    VerifyRes verify();
}
