package org.mbg.anm.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.filter.UserSecurityFilter;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.security.UserDetailServiceImpl;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.configuration.RsaProperties;
import org.mbg.common.security.configuration.SecurityConfiguration;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * Configuration class for Gateway Security in a WebFlux application.
 * This class sets up security rules, authentication mechanisms, CORS configuration, and integrates with Keycloak for
 * resource-based authorization.
 */
@Slf4j
@Configuration
public class AuthSecurityConfiguration extends SecurityConfiguration {

    private final AuthenticationProperties ap;

    private final CorsConfigurationSource corsConfigurationSource;

    private final JwtProvider jwtProvider;

    private final UserDetailServiceImpl userDetailsService;

    private final RsaProperties rsaProperties;

    public AuthSecurityConfiguration(SecurityProblemSupport problemSupport, AuthenticationProperties ap, CorsConfigurationSource corsConfigurationSource, JwtProvider jwtProvider, UserDetailServiceImpl userDetailsService, RsaProperties rsaProperties) {
        super(problemSupport);
        this.ap = ap;
        this.corsConfigurationSource = corsConfigurationSource;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.rsaProperties = rsaProperties;
    }

    @Override
    protected String[] getPublicUrlPatterns() {
        return this.ap.getAuthentication().getPublicUrlPatterns();
    }

    @Override
    protected AuthorizationFilter getAuthorizationFilter() {
        return new UserSecurityFilter(jwtProvider, userDetailsService);
    }

    @Override
    protected CorsConfigurationSource getCorsConfigurationSource() {
        return corsConfigurationSource;
    }

}