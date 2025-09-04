package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.jwt.JwtAccessToken;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.model.User;
import org.mbg.anm.model.dto.UserDTO;
import org.mbg.anm.repository.PermissionRepository;
import org.mbg.anm.repository.RoleRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.anm.service.TokenService;
import org.mbg.anm.service.UserService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.label.LabelKey;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.util.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final TokenService tokenService;

    private final RsaProvider rsaProvider;

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        return null;
    }

    @Override
    public JwtAccessToken login(UserDTO userDTO) {
        if (Validator.isNull(userDTO.getUsername()) || Validator.isNull(userDTO.getPassword())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (Validator.isNull(user)) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        String password;

        try {
            password = this.rsaProvider.decrypt(userDTO.getPassword());
        } catch (Exception e) {
            _log.error("login occurred an exception {}", e.getMessage());
            throw new BadRequestException(LabelKey.ERROR_INCORRECT_SIGNATURE, User.class.getName(), LabelKey.ERROR_INCORRECT_SIGNATURE);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }

        JwtAccessToken accessToken = jwtProvider.createAccessToken(userDTO.getUsername());

        this.tokenService.saveToken(userDTO.getUsername(), accessToken);
        this.tokenService.saveRefreshToken(userDTO.getUsername(), accessToken.getRefreshToken());

        return accessToken;

    }
}
