package com.hust.iam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.security.UserPrincipal;
import com.hust.common.base.model.JwtAccessToken;
import com.hust.iam.jwt.JwtProvider;
import com.hust.iam.model.User;
import com.hust.iam.model.dto.request.LoginReq;
import com.hust.common.base.configuration.ValidationProperties;
import com.hust.common.base.enums.ErrorCode;
import com.hust.common.base.model.dto.request.ChangePasswordReq;
import com.hust.common.base.model.dto.request.OtpReq;
import com.hust.common.base.model.dto.request.ResetPasswordReq;
import com.hust.common.base.model.dto.response.TransactionResponse;
import com.hust.common.base.model.dto.response.VerifyRes;
import com.hust.iam.repository.UserRepository;
import com.hust.iam.service.AuthService;
import com.hust.common.api.exception.BadRequestException;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.service.TransactionService;
import com.hust.common.label.LabelKey;
import com.hust.common.security.RsaProvider;
import com.hust.common.security.exception.UnauthorizedException;
import com.hust.common.util.Validator;
import com.hust.enums.OtpType;
import com.hust.service.OtpService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RsaProvider rsaProvider;

    private final OtpService otpService;

    private final TransactionService transactionService;

    private final ValidationProperties validationProperties;

    @Override
    public JwtAccessToken login(LoginReq userDTO) {
        if (Validator.isNull(userDTO.getUsername()) || Validator.isNull(userDTO.getPassword())) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        User user = userRepository.findByUsername(userDTO.getUsername());

        if (Validator.isNull(user)) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        if (Validator.equals(user.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        if (Validator.isNull(user) || !Validator.equals(user.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        String password = this.decryptPassword(userDTO.getPassword());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        return jwtProvider.createAccessToken(userDTO.getUsername());

    }

    @Override
    public JwtAccessToken refreshToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (Validator.isNull(auth)) {
            throw new UnauthorizedException(LabelKey.ERROR_INVALID_TOKEN);
        }

        String username = auth.getName();

        return this.jwtProvider.createAccessToken(username);
    }

    @Override
    public VerifyRes verify() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (Validator.isNull(auth)) {
            throw new BadRequestException(LabelKey.ERROR_UNAUTHORIZED, User.class.getName(), LabelKey.ERROR_UNAUTHORIZED);
        }

        VerifyRes verifyRes = new VerifyRes();

        verifyRes.setPermissions(auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        verifyRes.setUser(auth.getName());
        verifyRes.setOrg(auth.getPrincipal() instanceof UserPrincipal ? ((UserPrincipal) auth.getPrincipal()).getUserOrganization() : null);

        return verifyRes;
    }

    @Override
    public TransactionResponse resetPassword(ResetPasswordReq req) {
        if (Validator.isNull(req.getEmail()) && Validator.isNull(req.getPhone())) {
            throw new BadRequestException(ErrorCode.MSG1016);
        }

        User user;

        if (Validator.isNotNull(req.getPhone())) {
            user = this.userRepository.findByPhoneAndStatusNot(req.getPhone(), EntityStatus.DELETED.getStatus());
        } else {
            user = this.userRepository.findByEmailAndStatusNot(req.getEmail(), EntityStatus.DELETED.getStatus());
        }

        if (Validator.isNull(user)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        if (Validator.equals(user.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        String transactionId;

        if (Validator.isNotNull(req.getPhone())) {
            transactionId = this.otpService.sendOtpViaSms(req.getPhone(), OtpType.RESET_PASSWORD, false);
        } else {
            transactionId = this.otpService.sendOtpViaEmail(req.getEmail(), OtpType.RESET_PASSWORD, false);
        }

        this.transactionService.saveTransaction(transactionId, req);

        return TransactionResponse.builder().transactionId(transactionId).build();
    }

    @Override
    public void verify(OtpReq req) {
        ResetPasswordReq resetReq = this.transactionService.getTransaction(req.getTransactionId(), ResetPasswordReq.class);

        if (Validator.isNull(resetReq)) {
            throw new BadRequestException(ErrorCode.MSG1033);
        }

        User user;
        String input;

        if (Validator.isNotNull(resetReq.getPhone())) {
            input = resetReq.getPhone();
            user = this.userRepository.findByPhoneAndStatusNot(input, EntityStatus.DELETED.getStatus());
        } else {
            input = resetReq.getEmail();
            user = this.userRepository.findByEmailAndStatusNot(input, EntityStatus.DELETED.getStatus());
        }

        if (Validator.isNull(user)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        this.otpService.validateOtp(input, req.getTransactionId(), OtpType.RESET_PASSWORD, req.getOtp());

        String password = validationProperties.generateRandomPassword();

        user.setPassword(this.passwordEncoder.encode(password));

        this.userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordReq req) {

    }

    @Override
    public JwtAccessToken customerToken(Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (Validator.isNull(user)) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        if (Validator.equals(user.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        if (Validator.isNull(user) || !Validator.equals(user.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        return jwtProvider.createAccessToken(user.getUsername());
    }

    private String decryptPassword(String encryptedPassword) {
        String password;

        try {
            password = this.rsaProvider.decrypt(encryptedPassword);
        } catch (Exception e) {
            _log.error("login occurred an exception {}", e.getMessage());
            throw new BadRequestException(LabelKey.ERROR_INCORRECT_SIGNATURE, User.class.getName(), LabelKey.ERROR_INCORRECT_SIGNATURE);
        }

        return password;
    }
}
