package org.mbg.anm.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.Client;
import org.mbg.anm.model.Permission;
import org.mbg.anm.model.Role;
import org.mbg.anm.model.User;
import org.mbg.anm.repository.ClientRepository;
import org.mbg.anm.repository.PermissionRepository;
import org.mbg.anm.repository.RoleRepository;
import org.mbg.anm.repository.UserRepository;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.exception.UserNotActivatedException;
import org.mbg.common.util.Validator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (!Validator.equals(user.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_YOUR_ACCOUNT_HAS_BEEN_LOCKED),
                    User.class.getName(), LabelKey.ERROR_YOUR_ACCOUNT_HAS_BEEN_LOCKED);
        }

        if (user == null) {
            _log.error("User not exist with name :{}", username);

            return null;
        }

        return this.createSpringSecurityUser(username, user);
    }

    public UserDetails loadUserByClientId(String clientId) throws UsernameNotFoundException {
        Client client = clientRepository.findByClientIdAndStatus(clientId, EntityStatus.ACTIVE.getStatus());

        if (client == null) {
            _log.error("client not exist with id :{}", clientId);

            return null;
        }

        return this.createSpringSecurityUser(clientId, client);
    }

    private UserDetails createSpringSecurityUser(String clientId, Client client) {
        if (!Validator.equals(EntityStatus.ACTIVE.getStatus(), client.getStatus())) {
            throw new UserNotActivatedException("Client " + clientId + " was not activated");
        }

        List<Role> roles = this.roleRepository.findByClientId(clientId);

        List<String> roleNames = new ArrayList<>();

        List<Permission> privileges = new ArrayList<>();

        roles.forEach(role -> {
            if (Validator.equals(role.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                roleNames.add(role.getCode());

                //get privilege by role id
                List<Permission> pves = this.permissionRepository.findByRoleCode(role.getCode());

                // set list of privileges into role
                role.setPermissions(pves);

                privileges.addAll(pves);
            }
        });

        List<GrantedAuthority> grantedAuthorities = privileges.stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode())).collect(Collectors.toList());

        // add role name to authority list
        roleNames.forEach(roleName -> grantedAuthorities.add(new SimpleGrantedAuthority(roleName)));

        return new UserPrincipal(client, roleNames, grantedAuthorities);
    }

    private UserPrincipal createSpringSecurityUser(String username, User user) {
        if (!Validator.equals(EntityStatus.ACTIVE.getStatus(), user.getStatus())) {
            throw new UserNotActivatedException("User " + username + " was not activated");
        }

        List<Role> roles = this.roleRepository.findByUserId(user.getId());

        // set list of roles into user
        user.setRoles(roles);

        List<String> roleNames = new ArrayList<>();

        List<Permission> privileges = new ArrayList<>();

        roles.forEach(role -> {
            if (Validator.equals(role.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                roleNames.add(role.getCode());

                //get privilege by role id
                List<Permission> pves = this.permissionRepository.findByRoleCode(role.getCode());

                // set list of privileges into role
                role.setPermissions(pves);

                privileges.addAll(pves);
            }
        });

        List<GrantedAuthority> grantedAuthorities = privileges.stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode())).collect(Collectors.toList());

        // add role name to authority list
        roleNames.forEach(roleName -> grantedAuthorities.add(new SimpleGrantedAuthority(roleName)));

        return new UserPrincipal(user, roleNames, grantedAuthorities);

    }
}
