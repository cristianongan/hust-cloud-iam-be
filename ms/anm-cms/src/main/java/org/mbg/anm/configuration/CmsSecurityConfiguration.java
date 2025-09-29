package org.mbg.anm.configuration;

import org.mbg.common.base.filter.ServiceSecurityFilter;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.configuration.SecurityConfiguration;
import org.mbg.common.security.filter.AuthorizationFilter;
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
