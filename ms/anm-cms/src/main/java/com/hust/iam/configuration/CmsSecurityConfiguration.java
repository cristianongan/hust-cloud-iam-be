package com.hust.iam.configuration;

import com.hust.common.base.filter.ServiceSecurityFilter;
import com.hust.common.security.configuration.AuthenticationProperties;
import com.hust.common.security.configuration.SecurityConfiguration;
import com.hust.common.security.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
public class CmsSecurityConfiguration extends SecurityConfiguration {
    private final AuthenticationProperties ap;

    private final CorsConfigurationSource corsConfigurationSource;

    public CmsSecurityConfiguration(SecurityProblemSupport problemSupport, AuthenticationProperties ap, CorsConfigurationSource corsConfigurationSource) {
        super(problemSupport);
        this.ap = ap;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Override
    protected String[] getPublicUrlPatterns() {
        return this.ap.getAuthentication().getPublicUrlPatterns();
    }

    @Override
    protected AuthorizationFilter getAuthorizationFilter() {
        return new ServiceSecurityFilter();
    }

    @Override
    protected CorsConfigurationSource getCorsConfigurationSource() {
        return corsConfigurationSource;
    }
}
