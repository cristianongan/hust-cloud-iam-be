package com.hust.common.security;

import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;


/**
 * 07/04/2025 - LinhLH: Create new
 *
 * @author LinhLH - ok
 */
public class SecurityExpressionRootImpl extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	
    private final Authentication authentication;

	private Object filterObject;
	private Object returnObject;
	private Object target;

	public SecurityExpressionRootImpl(Authentication authentication) {
		super(authentication);
		this.authentication = authentication;
	}

	public final boolean isAdministrator() {
		return hasRole(SecurityConstants.SystemRole.SUPER_ADMIN);
	}

	public boolean hasPrivilege(String privilege) {
		return isAdministrator() || hasAdministratorPrivilege(privilege) || hasAuthority(privilege);
	}

	public boolean hasPrivilege(String privilege, String scope) {
		return hasPrivilege(String.join(StringPool.COLON, privilege, scope));
	}
	
	public boolean hasAnyPrivilege(String... privileges) {
		return isAdministrator() || hasAnyAuthority(privileges);
	}
	
	public boolean hasAdministratorPrivilege(String privilege) {
		String[] parts = StringUtils.split(privilege, StringPool.UNDERLINE);

		if (parts.length == 0) {
			return false;
		}

		return hasAuthority(
				parts[0] + StringPool.UNDERLINE + SecurityConstants.Privilege.ADMINISTRATOR);
	}
	
	@Override
	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
		if (role == null) {
			return null;
		}

		if ((defaultRolePrefix == null) || (defaultRolePrefix.isEmpty())) {
			return role;
		}

		if (role.startsWith(defaultRolePrefix)) {
			return role;
		}

		return defaultRolePrefix + role;
	}

	@Override
	public Object getFilterObject() {
		return this.filterObject;
	}

	@Override
	public Object getReturnObject() {
		return this.returnObject;
	}

	@Override
	public Object getThis() {
		return this.target;
	}

	@Override
	public void setFilterObject(Object obj) {
		this.filterObject = obj;
	}

	@Override
	public void setReturnObject(Object obj) {
		this.returnObject = obj;
	}
}
