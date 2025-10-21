package org.mbg.anm.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.base.configuration.ValidationProperties;
import org.mbg.anm.fiegn.CmsClient;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.UserRole;
import org.mbg.anm.repository.*;
import org.mbg.anm.security.UserDetailServiceImpl;
import org.mbg.anm.security.UserPrincipal;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.enums.UserType;
import org.mbg.anm.model.User;
import org.mbg.common.base.model.dto.UserDTO;
import org.mbg.common.base.model.dto.request.UserReq;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.service.UserService;
import org.mbg.anm.service.mapper.UserMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.model.dto.QuotaDTO;
import org.mbg.common.base.model.dto.request.QuotaBatchReq;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final PasswordEncoder passwordEncoder;

    private final RsaProvider rsaProvider;

    private final UserMapper userMapper;

    private final ValidationProperties validationProperties;

    private final UserDetailServiceImpl userDetailsService;

    private Pattern passwordPattern;

    private final UserRoleRepository userRoleRepository;

    private final CmsClient cmsClient;

    @PostConstruct
    protected void init() {
        this.passwordPattern = Pattern.compile(this.validationProperties.getPasswordRegex());
    }

    @Override
    public UserDTO detail() {
        Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();

        if (Validator.isNull(userDetails)) {
            throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND));
        }

        User user = this.userRepository.findByUsername(userDetails.getName());

        if (Validator.isNull(user)) {
            throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND));
        }

        UserDTO userDTO = userMapper.toDto(user);

        userDTO.setPermissions(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO update(UserReq userReq) {
        if (Validator.isNull(userReq.getId())) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        User user = userRepository.findById(userReq.getId()).orElse(null);

        if (Validator.isNull(user)) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        this.validateUserReq(userReq);

        if (Validator.isNotNull(userReq.getPassword())) {
            String password = this.decryptPassword(userReq.getPassword());

            if (!passwordPattern.matcher(password).matches()) {
                throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INVALID_DATA_FORMAT,
                        new String[]{Labels.getLabels(LabelKey.LABEL_PASSWORD)})
                        , User.class.getName(), LabelKey.ERROR_INVALID_DATA_FORMAT);
            }

            if (Validator.isNotNull(password) && !this.passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(this.passwordEncoder.encode(password));
            }
        }

        if (!Validator.equals(user.getEmail(), userReq.getEmail()) &&
            this.userRepository.existsByEmailAndStatusNot(userReq.getEmail(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[]{Labels.getLabels(LabelKey.LABEL_EMAIL)})
                    , User.class.getName(), LabelKey.ERROR_DUPLICATE_DATA);
        }

        if (!Validator.equals(userReq.getPhone(), user.getPhone()) &&
            this.userRepository.existsByPhoneAndStatusNot(userReq.getPhone(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[]{Labels.getLabels(LabelKey.LABEL_PHONE_NUMBER)})
                    , User.class.getName(), LabelKey.ERROR_DUPLICATE_DATA);
        }

        user.setPhone(userReq.getPhone());
        user.setEmail(userReq.getEmail());
        user.setAddress(userReq.getAddress());
        user.setDob(userReq.getDob());
        user.setGender(userReq.getGender());
        user.setFullname(userReq.getFullname());
        user.setType(userReq.getType());
        user.setStatus(userReq.getStatus());

        return this.userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO create(UserReq userReq) {
        if (Validator.isNull(userReq.getStatus())) {
            userReq.setStatus(EntityStatus.ACTIVE.getStatus());
        }

        if (Validator.equals(userReq.getStatus(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID,
                    User.class.getName(), LabelKey.ERROR_INVALID);
        }

        this.validateUserReq(userReq);

        if (Validator.isNull(userReq.getUsername()) || Validator.isNull(userReq.getPassword())) {
            throw new BadRequestException(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD,
                    User.class.getName(), LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }

        String password = this.decryptPassword(userReq.getPassword());

        if (!passwordPattern.matcher(password).matches()) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INVALID_DATA_FORMAT,
                    new String[]{Labels.getLabels(LabelKey.LABEL_PASSWORD)})
                    , User.class.getName(), LabelKey.ERROR_INVALID_DATA_FORMAT);
        }

        if (this.userRepository.existsByUsername(userReq.getUsername())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[]{Labels.getLabels(LabelKey.LABEL_USERNAME)})
                    , User.class.getName(), LabelKey.ERROR_DUPLICATE_DATA);
        }

        if (this.userRepository.existsByPhoneAndStatusNot(userReq.getPhone(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[]{Labels.getLabels(LabelKey.LABEL_PHONE_NUMBER)})
                    , User.class.getName(), LabelKey.ERROR_DUPLICATE_DATA);
        }

        if (this.userRepository.existsByEmailAndStatusNot(userReq.getEmail(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DUPLICATE_DATA,
                    new String[]{Labels.getLabels(LabelKey.LABEL_EMAIL)})
                    , User.class.getName(), LabelKey.ERROR_DUPLICATE_DATA);
        }

        User user = new User();
        user.setUsername(userReq.getUsername());
        user.setPhone(userReq.getPhone());
        user.setEmail(userReq.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setAddress(userReq.getAddress());
        user.setDob(userReq.getDob());
        user.setGender(userReq.getGender());
        user.setFullname(userReq.getFullname());
        user.setType(userReq.getType());
        user.setStatus(userReq.getStatus());

        return this.userMapper.toDto(this.userRepository.save(user));
    }

    @Override
    public UserDTO customerCreate(UserReq userReq) {
        if (Validator.isNull(userReq.getStatus())) {
            userReq.setStatus(EntityStatus.ACTIVE.getStatus());
        }

        if (Validator.equals(userReq.getStatus(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1016);
        }

        if (Validator.isNull(userReq.getUsername()) || Validator.isNull(userReq.getPassword())) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        String password = this.decryptPassword(userReq.getPassword());

        if (!passwordPattern.matcher(password).matches()) {
            throw new BadRequestException(ErrorCode.MSG1004);
        }

        if (this.userRepository.existsByUsername(userReq.getUsername())) {
            throw new BadRequestException(ErrorCode.MSG1041);
        }

        if (this.userRepository.existsByPhoneAndStatusNot(userReq.getPhone(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1044);
        }

        if (this.userRepository.existsByEmailAndStatusNot(userReq.getEmail(), EntityStatus.DELETED.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1045);
        }

        User user = new User();
        user.setUsername(userReq.getUsername());
        user.setPhone(userReq.getPhone());
        user.setEmail(userReq.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setAddress(userReq.getAddress());
        user.setDob(userReq.getDob());
        user.setGender(userReq.getGender());
        user.setFullname(userReq.getFullname());
        user.setType(userReq.getType());
        user.setStatus(userReq.getStatus());

        return this.userMapper.toDto(this.userRepository.save(user));
    }

    private void validateUserReq(UserReq userReq) {
        if (Validator.isNull(userReq.getFullname())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_FULLNAME)})
                    , User.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        if (Validator.isNull(userReq.getEmail())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_EMAIL)})
                    , User.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        if (Validator.isNull(userReq.getPhone())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_PHONE_NUMBER)})
                    , User.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        if (Validator.isNull(userReq.getStatus()) || Validator.isNull(EntityStatus.valueOfStatus(userReq.getStatus()))) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_STATUS)})
                    , User.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        if (Validator.isNull(userReq.getType()) || Validator.isNull(UserType.valueOfStatus(userReq.getType()))) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_TYPE)})
                    , User.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }
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

    @Override
    public Page<UserDTO> searchUsers(UserSearch search) {

        Pageable pageable = PageRequest.of(search.getPage(), search.getPageSize());

        List<User> users = this.userRepository.search(search, pageable);

        List<UserDTO> content = this.userMapper.toDto(users);

        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());

        List<UserRole> userRoles = this.userRoleRepository.findByUserIdIn(ids, EntityStatus.ACTIVE.getStatus());
        Map<Long , List<String>> roleMap = userRoles.stream().collect(Collectors.groupingBy(
                UserRole::getUserId,
                Collectors.mapping(UserRole::getRoleCode
                        , Collectors.toList())
        ));

        List<QuotaDTO> quotas = this.cmsClient.getBatch(QuotaBatchReq.builder().userIds(ids).build());

        Map<Long, QuotaDTO> quotaMap = Validator.isNotNull(quotas) ?
                quotas.stream().collect(Collectors.toMap(QuotaDTO::getUserId, r -> r, (a, b) -> a,                            // nếu trùng key, giữ bản a (hoặc b)
                        HashMap::new))
                : new HashMap<>();

        content.forEach(user -> {
            if (roleMap.containsKey(user.getId())) {
                user.setRoles(roleMap.get(user.getId()));
            }

            if (quotaMap.containsKey(user.getId())) {
                user.setQuota(quotaMap.get(user.getId()));
            }
        });

        Long count  = this.userRepository.count(search);

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    @Transactional
    public void updateStatus(UserReq userReq) {
        if (Validator.isNotNull(userReq.getIds()) && Validator.isNotNull(userReq.getStatus())
            && Validator.isNotNull(EntityStatus.valueOfStatus(userReq.getStatus()))
        ) {
            if (Validator.equals(userReq.getStatus(), EntityStatus.DELETED)) {
                throw new BadRequestException(LabelKey.ERROR_INVALID,
                        User.class.getName(), LabelKey.ERROR_INVALID);
            }

            this.userRepository.updateStatusByIdIn(userReq.getStatus(), userReq.getIds());
        }
    }

    @Override
    @Transactional
    public void delete(UserReq userReq) {
        if (Validator.isNotNull(userReq.getIds())) {
            validateSelfLock(userReq.getIds());

            this.userRepository.updateStatusByIdIn(EntityStatus.DELETED.getStatus(), userReq.getIds());
            this.clientRepository.updateStatusByUserIds(userReq.getIds(), EntityStatus.DELETED.getStatus());
        }
    }

    @Override
    @Transactional
    public void assignRole(UserReq userReq) {
        if (Validator.isNull(userReq.getId())) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        User user = userRepository.findById(userReq.getId()).orElse(null);

        if (Validator.isNull(user)) {
            throw new BadRequestException(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND,
                    User.class.getName(), LabelKey.ERROR_USER_COULD_NOT_BE_FOUND);
        }

        this.userRoleRepository.removeAllRoleByUserId(user.getId());

        if (Validator.isNull(userReq.getRoles())) {
            return;
        }

        List<Role> roles = this.roleRepository.findByCodeIn(userReq.getRoles());

        List<UserRole> userRoles = new ArrayList<>();

        if (Validator.isNotNull(roles)) {
            for (Role role : roles) {
                UserRole userRole = new UserRole();

                userRole.setUserId(user.getId());
                userRole.setRoleCode(role.getCode());
                userRoles.add(userRole);
            }

            this.userRoleRepository.saveAll(userRoles);
        }


    }

    @Override
    public void disable(UserReq userReq) {
        if (Validator.isNotNull(userReq.getIds())) {
            validateSelfLock(userReq.getIds());

            this.userRepository.updateStatusByIdIn(EntityStatus.INACTIVE.getStatus(), userReq.getIds());
            this.clientRepository.updateStatusByUserIds(userReq.getIds(), EntityStatus.INACTIVE.getStatus());
        }
    }

    @Override
    public void enable(UserReq userReq) {
        if (Validator.isNotNull(userReq.getIds())) {
            this.userRepository.updateStatusByIdIn(EntityStatus.ACTIVE.getStatus(), userReq.getIds());
            this.clientRepository.updateStatusByUserIds(userReq.getIds(), EntityStatus.ACTIVE.getStatus());
        }
    }

    private void validateSelfLock(List<Long> ids) {
        Long userId = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            userId = userPrincipal.getUserId();
        } catch (Exception e) {
            throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_USER_COULD_NOT_BE_FOUND));
        }

        if (ids.contains(userId)) {
            throw new BadRequestException(LabelKey.ERROR_YOU_CANNOT_LOCK_YOURSELF,
                    User.class.getName(), LabelKey.ERROR_YOU_CANNOT_LOCK_YOURSELF);
        }
    }
}
