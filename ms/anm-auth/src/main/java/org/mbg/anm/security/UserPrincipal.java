package org.mbg.anm.security;

import org.mbg.anm.model.Client;
import org.mbg.anm.model.User;
import org.mbg.common.util.Validator;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

public class UserPrincipal extends org.springframework.security.core.userdetails.User implements Serializable {

    @Serial
    private static final long serialVersionUID = 3423840476507238116L;

    private User user;

    private Client client;

    private String organization;

    private Collection<String> roles;

    public UserPrincipal(User user, Collection<String> roles, String organization,Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.organization = organization;
        this.user = user;
        this.roles = roles;
    }

    public UserPrincipal(Client client, Collection<String> roles, String organization,Collection<? extends GrantedAuthority> authorities) {
        super(client.getClientId(), client.getClientSecret(), authorities);
        this.organization = organization;
        this.client = client;
        this.roles = roles;
    }

    public Long getUserId() {
        return Validator.isNotNull(user) ? user.getId() : client.getId();
    }

    @Override
    public String getUsername() {
        return Validator.isNotNull(user) ? user.getUsername() : client.getClientId();
    }

    @Override
    public String getPassword() {
        return Validator.isNotNull(user) ? user.getPassword() : client.getClientSecret();
    }

    public String getUserOrganization() {
        return organization;
    }
}
