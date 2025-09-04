package org.mbg.anm.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.filter.UserSecurityFilter;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.configuration.RsaProperties;
import org.mbg.common.security.configuration.SecurityConfiguration;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Configuration class for Gateway Security in a WebFlux application.
 * This class sets up security rules, authentication mechanisms, CORS configuration, and integrates with Keycloak for
 * resource-based authorization.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthSecurityConfiguration extends SecurityConfiguration {

    private final AuthenticationProperties ap;

    private final CorsConfigurationSource corsConfigurationSource;

    private final JwtProvider jwtProvider;

    private final RsaProperties rsaProperties;

    @Override
    protected String[] getPublicUrlPatterns() {
        return this.ap.getAuthentication().getPublicUrlPatterns();
    }

    @Override
    protected AuthorizationFilter getAuthorizationFilter() {
        return new UserSecurityFilter(jwtProvider);
    }

    @Override
    protected CorsConfigurationSource getCorsConfigurationSource() {
        return corsConfigurationSource;
    }

}