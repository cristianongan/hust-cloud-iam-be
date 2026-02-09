package com.hust.iam.service;

import com.hust.common.base.model.JwtAccessToken;
import com.hust.iam.model.dto.request.LoginReq;
import com.hust.common.base.model.dto.request.ChangePasswordReq;
import com.hust.common.base.model.dto.request.OtpReq;
import com.hust.common.base.model.dto.request.ResetPasswordReq;
import com.hust.common.base.model.dto.response.TransactionResponse;
import com.hust.common.base.model.dto.response.VerifyRes;

public interface AuthService {
    JwtAccessToken login(LoginReq userDTO);

    JwtAccessToken refreshToken();

    VerifyRes verify();

    TransactionResponse resetPassword(ResetPasswordReq req);

    void verify(OtpReq req);

    void changePassword(ChangePasswordReq req);

    JwtAccessToken customerToken(Long userId);
}
