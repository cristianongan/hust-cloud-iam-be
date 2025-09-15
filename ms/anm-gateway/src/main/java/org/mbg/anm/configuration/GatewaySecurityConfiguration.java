package org.mbg.anm.configuration;

import org.mbg.anm.filer.SecurityFilter;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.anm.util.GatewayConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.HeaderWriterServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for Gateway Security in a WebFlux application.
 * This class sets up security rules, authentication mechanisms, CORS configuration, and integrates with Keycloak for
 * resource-based authorization.
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity // MANDATORY: Enables Spring Security for a WebFlux application
@EnableRedisWebSession
@RequiredArgsConstructor
public class GatewaySecurityConfiguration {
    private final AuthenticationProperties ap;

    private final CorsConfiguration corsConfiguration;

    private final WebClient.Builder webClientBuilder;

    private final ClientApiProperties webClientApiProperties;

    /**
     * Defines the primary security filter chain for the Gateway.
     *
     * @param http The {@link ServerHttpSecurity} to configure.
     * @return The configured {@link SecurityWebFilterChain}.
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // 1. Configure Authorization Rules
                .authorizeExchange(exchanges -> exchanges
                        // Allow access to public paths defined in SecurityConstants
                        // Ensure SecurityConstants.PublicUrlPattern.getPublicUrlPattern() returns String[]
                        .pathMatchers(SecurityConstants.PublicUrlPattern.getPublicUrlPattern()).permitAll()
                        .pathMatchers(this.ap.getAuthentication().getPublicUrlPatterns()).permitAll()
                        // Any other request requires the user to be authenticated
                        .anyExchange().authenticated()
                )
                .addFilterAt(new SecurityFilter(webClientBuilder, webClientApiProperties), SecurityWebFiltersOrder.AUTHENTICATION)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Example: Enable CORS based on other beans/config
        // .headers(headers -> headers.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable)) // Example
        ;

        // Build and return the security filter chain
        return http.build();
    }

    /**
     * Configures and returns a ServerOAuth2AuthorizationRequestResolver bean, which resolves authorization
     * requests for OAuth2 login or client credential flows. This implementation supports using Proof Key
     * for Code Exchange (PKCE) by customizing the authorization request.
     *
     * @param clientRegistrationRepository the ReactiveClientRegistrationRepository used to manage client
     *                                     registrations for performing OAuth2 operations.
     * @return a configured ServerOAuth2AuthorizationRequestResolver to handle OAuth2 authorization requests.
     */
//    @Bean
//    ServerOAuth2AuthorizationRequestResolver requestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
//
//        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
//
//        return resolver;
//    }

    /**
     * Creates and returns an instance of {@link ServerOAuth2AuthorizedClientRepository}.
     * This repository is used to manage the storage of authorized OAuth2 clients in the context
     * of a web session for reactive applications.
     *
     * @return a {@link ServerOAuth2AuthorizedClientRepository} implementation using web sessions for storage.
     */
//    @Bean
//    ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
//        return new WebSessionServerOAuth2AuthorizedClientRepository();
//    }

    /**
     * Configures and provides a {@link ServerLogoutSuccessHandler} bean for handling successful logout.
     * The method utilizes {@link OidcClientInitiatedServerLogoutSuccessHandler} to manage OpenID Connect (OIDC)
     * logout flows and sets a post-logout redirect URI.
     *
     * @param clientRegistrationRepository the {@link ReactiveClientRegistrationRepository} used to retrieve
     *                                     registered client information for OIDC logout.
     * @return a configured {@link ServerLogoutSuccessHandler} to handle logout operations.
     */
//    @Bean
//    ServerLogoutSuccessHandler logoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
//                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
//
//        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(GatewayConstant.Uri.LOGOUT_URI);
//
//        return oidcLogoutSuccessHandler;
//    }

    /**
     * Configures and provides a {@link ServerLogoutHandler} bean to manage server-side logout operations.
     * This method utilizes a combination of strategies for handling logout:
     * - {@link SecurityContextServerLogoutHandler} clears the security context.
     * - {@link WebSessionServerLogoutHandler} invalidates the associated web session.
     * - {@link HeaderWriterServerLogoutHandler} removes cookies using {@link ClearSiteDataServerHttpHeadersWriter}.
     *
     * @return a configured {@link ServerLogoutHandler} to handle logout operations.
     */
//    @Bean
//    ServerLogoutHandler logoutHandler() {
//        return new DelegatingServerLogoutHandler(
//                new SecurityContextServerLogoutHandler(),
//                new WebSessionServerLogoutHandler(),
//                new HeaderWriterServerLogoutHandler(
//                        new ClearSiteDataServerHttpHeadersWriter(ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES)
//                )
//        );
//    }

    /**
     * Provides a primary {@link ReactiveRedisConnectionFactory} bean to establish a reactive connection to a Redis database
     * using the given Redisson configuration.
     *
     * @param redissonConfig the configuration object for Redisson, used to create the connection factory
     * @return an instance of {@link ReactiveRedisConnectionFactory} created with the provided Redisson configuration
     */
    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(Config redissonConfig) {
        return new RedissonConnectionFactory(redissonConfig);
    }

    /**
     * Configures and provides a {@link CorsConfigurationSource} bean for handling Cross-Origin Resource Sharing (CORS)
     * requests in the application. If allowed origins are specified in the configuration, the method registers the CORS
     * settings for all URL patterns. Otherwise, the registration is skipped, and a warning is logged.
     *
     * @return an instance of {@link CorsConfigurationSource}, either configured with CORS settings or unregistered if no
     * allowed origins are specified.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Register the configuration only if origins are specified
        if (corsConfiguration.getAllowedOriginPatterns() != null
                && !corsConfiguration.getAllowedOriginPatterns().isEmpty()) {
            _log.info("[corsConfigurationSource-WebFlux] Registering CORS configuration for pattern: {}", SecurityConstants.UrlPattern.ALL);
            source.registerCorsConfiguration(SecurityConstants.UrlPattern.ALL, corsConfiguration);
        } else {
            _log.warn("[corsConfigurationSource-WebFlux] CORS filter registration skipped: No allowed origins configured.");
        }

        return source;
    }
    // --- Notes ---
    // - No need to explicitly add GlobalFilter beans here (like GatewayAuthorizationFilter).
    //   Spring Cloud Gateway discovers and applies them based on their @Component/@Order.
    // - Ensure your application.yml has the correct 'spring.security.oauth2.resourceserver.jwt.issuer-uri'.
    // - The order of your custom GlobalFilter relative to these security filters is important.
    //   The security filters configured here (especially authentication) will generally run early.
    //   Your enrichment filter should have an order that ensures it runs *after* successful authentication.
}