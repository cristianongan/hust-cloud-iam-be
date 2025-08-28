package org.mbg.common.keycloak.configuration;

import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.configuration.SecurityConfiguration;
import org.mbg.common.security.filter.AuthorizationFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author: LinhLH
 **/

@Getter
@Configuration
@RequiredArgsConstructor
public class KeycloakSecurityConfiguration extends SecurityConfiguration {
    private final AuthorizationFilter authorizationFilter;

    private final CorsConfigurationSource corsConfigurationSource;

    private final AuthenticationProperties ap;

    @Override
    protected String[] getPublicUrlPatterns() {
        return ap.getAuthentication().getPublicUrlPatterns();
    }

    @Override
    protected Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> getOAuth2ResourceServerConfigurer() {
        return oauth2 -> oauth2.jwt(Customizer.withDefaults());
    }
}
