package org.mbg.anm.configuration;

import lombok.RequiredArgsConstructor;
import org.mbg.common.base.filter.ServiceSecurityFilter;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.configuration.SecurityConfiguration;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class ProducerSecurityConfiguration extends SecurityConfiguration {
    private final AuthenticationProperties ap;

    private final CorsConfigurationSource corsConfigurationSource;

    @Override
    protected String[] getPublicUrlPatterns() {
        return new String[0];
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
