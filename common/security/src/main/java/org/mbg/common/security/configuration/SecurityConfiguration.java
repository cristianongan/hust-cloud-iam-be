package org.mbg.common.security.configuration;

import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.common.security.util.SecurityConstants;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Abstract base configuration class for Spring Security settings within VMC applications.
 * <p>
 * This class enables web security and method-level security, providing a foundational
 * {@link SecurityFilterChain} bean. It establishes common security practices such as
 * disabling CSRF, enforcing stateless session management suitable for APIs, and integrating
 * OAuth2 resource server capabilities for token validation.
 * </p>
 * <p>
 * Subclasses are required to implement abstract methods to define application-specific
 * public URL patterns and the precise configuration for the OAuth2 resource server (e.g., JWT validation).
 * It also registers a custom {@link AuthorizationFilter} before the standard Spring Security filters.
 * </p>
 *
 * @author: LinhLH
 * @see EnableWebSecurity
 * @see EnableMethodSecurity
 * @see SecurityFilterChain
 * @see AuthorizationFilter
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable method-level security annotations like @PreAuthorize
@NoArgsConstructor
public abstract class SecurityConfiguration {

    /**
     * Defines the primary security filter chain for the application.
     * <p>
     * Configures core security aspects:
     * <ul>
     *     <li>Disables CSRF protection (common for stateless APIs).</li>
     *     <li>Sets session management to STATELESS.</li>
     *     <li>Configures HTTP request authorization:
     *         <ul>
     *             <li>Permits access to default public URLs (actuator, login, refresh).</li>
     *             <li>Permits access to application-specific public URLs provided by {@link #getPublicUrlPatterns()}.</li>
     *             <li>Requires authentication for any other request.</li>
     *         </ul>
     *     </li>
     *     <li>Integrates OAuth2 resource server support using configuration provided by {@link #getOAuth2ResourceServerConfigurer()}.</li>
     *     <li>Adds the custom {@link AuthorizationFilter} before the {@link UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     * </p>
     *
     * @param http The {@link HttpSecurity} to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless sessions
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(getDefaultPublicUrlPatterns()).permitAll() // Allow default public URLs
                        .requestMatchers(getPublicUrlPatterns()).permitAll() // Allow custom public URLs
                        .anyRequest().authenticated() // Secure all other requests
                )
                .oauth2ResourceServer(getOAuth2ResourceServerConfigurer()) // Configure OAuth2 resource server (JWT validation)
                .cors(cors -> cors.configurationSource(getCorsConfigurationSource())) // Enable CORS
                .addFilterBefore(getAuthorizationFilter(), org.springframework.security.web.access.intercept.AuthorizationFilter.class); // Add our custom filter

        return http.build();
    }

    /**
     * Abstract method to be implemented by subclasses to provide application-specific
     * URL patterns that should be publicly accessible without authentication.
     *
     * @return An array of public URL patterns.
     */
    protected abstract String[] getPublicUrlPatterns();

    protected abstract AuthorizationFilter getAuthorizationFilter();

    protected abstract CorsConfigurationSource getCorsConfigurationSource();

    /**
     * Provides a default set of URL patterns that are typically public.
     * Includes actuator endpoints, login, and token refresh paths.
     *
     * @return An array of default public URL patterns.
     */
    private String[] getDefaultPublicUrlPatterns() {
        return SecurityConstants.PublicUrlPattern.getPublicUrlPattern();
    }

    /**
     * Abstract method to be implemented by subclasses to provide the specific
     * configuration customizer for the OAuth2 resource server. This typically
     * involves setting up JWT validation (e.g., specifying the JWK Set URI or issuer URI).
     *
     * @return A {@link Customizer} for {@link OAuth2ResourceServerConfigurer}.
     */
    protected abstract Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> getOAuth2ResourceServerConfigurer();
}