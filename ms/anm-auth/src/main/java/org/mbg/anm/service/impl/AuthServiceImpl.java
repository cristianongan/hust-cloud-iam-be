package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.model.User;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.common.base.configuration.ValidationProperties;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.model.dto.request.ChangePasswordReq;
import org.mbg.common.base.model.dto.request.OtpReq;
import org.mbg.common.base.model.dto.request.ResetPasswordReq;
import org.mbg.common.base.model.dto.response.TransactionResponse;
import org.mbg.common.base.model.dto.response.VerifyRes;
import org.mbg.anm.repository.PermissionRepository;
import org.mbg.anm.repository.RoleRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.anm.security.UserDetailServiceImpl;
import org.mbg.anm.service.AuthService;
import org.mbg.anm.service.TokenService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.service.TransactionService;
import org.mbg.common.label.LabelKey;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.common.util.Validator;
import org.mbg.enums.OtpType;
import org.mbg.service.OtpService;
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
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }

        User user = userRepository.findByUsername(userDTO.getUsername());

        if (Validator.equals(user.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        if (Validator.isNull(user) || !Validator.equals(user.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        String password = this.decryptPassword(userDTO.getPassword());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
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
