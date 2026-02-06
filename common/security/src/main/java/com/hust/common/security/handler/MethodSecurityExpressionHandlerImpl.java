package com.hust.common.security.handler;

import com.hust.common.security.SecurityExpressionRootImpl;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * 07/04/2025 - LinhLH: Create new
 *
 * @author LinhLH - ok
 */

public class MethodSecurityExpressionHandlerImpl extends DefaultMethodSecurityExpressionHandler {
	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
			MethodInvocation invocation) {
		SecurityExpressionRootImpl root = new SecurityExpressionRootImpl(authentication);

		root.setTrustResolver(new AuthenticationTrustResolverImpl());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setRoleHierarchy(getRoleHierarchy());

		return root;
	}

	@Override
	public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
		return createEvaluationContext(authentication.get(), mi);
	}
}
