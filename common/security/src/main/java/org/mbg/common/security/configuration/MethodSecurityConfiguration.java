package org.mbg.common.security.configuration;

import org.mbg.common.security.handler.MethodSecurityExpressionHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


/**
 * @author LinhLH - ok
 *
 */
@Configuration
@Role(2)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class MethodSecurityConfiguration {

	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
        return new MethodSecurityExpressionHandlerImpl();
	}
}
