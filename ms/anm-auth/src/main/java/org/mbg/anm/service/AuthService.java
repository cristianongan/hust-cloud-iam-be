package org.mbg.anm.service;

import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.common.base.model.dto.request.ChangePasswordReq;
import org.mbg.common.base.model.dto.request.OtpReq;
import org.mbg.common.base.model.dto.request.ResetPasswordReq;
import org.mbg.common.base.model.dto.response.TransactionResponse;
import org.mbg.common.base.model.dto.response.VerifyRes;

public interface AuthService {
    JwtAccessToken login(LoginReq userDTO);

    JwtAccessToken refreshToken();

    VerifyRes verify();

    TransactionResponse resetPassword(ResetPasswordReq req);

    void verify(OtpReq req);

    void changePassword(ChangePasswordReq req);
}
