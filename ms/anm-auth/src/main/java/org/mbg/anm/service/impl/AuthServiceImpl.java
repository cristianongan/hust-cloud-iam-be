package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.model.User;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.common.base.model.dto.response.VerifyRes;
import org.mbg.anm.repository.PermissionRepository;
import org.mbg.anm.repository.RoleRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.anm.security.UserDetailServiceImpl;
import org.mbg.anm.service.AuthService;
import org.mbg.anm.service.TokenService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.label.LabelKey;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.common.util.Validator;
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

    private final TokenService tokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RsaProvider rsaProvider;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final UserDetailServiceImpl userDetailsService;

    @Override
    public JwtAccessToken login(LoginReq userDTO) {
        if (Validator.isNull(userDTO.getUsername()) || Validator.isNull(userDTO.getPassword())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
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
