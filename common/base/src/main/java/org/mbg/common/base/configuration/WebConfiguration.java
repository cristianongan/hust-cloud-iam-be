package org.mbg.common.base.configuration;

import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.StringPool;
import org.mbg.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

/**
 * Configures Spring Web MVC settings, primarily focusing on Cross-Origin Resource Sharing (CORS)
 * and request interceptors.
 * <p>
 * This configuration class defines beans for CORS handling (both via {@link CorsFilter}
 * and {@link WebMvcConfigurer})
 * Configuration values for CORS are sourced from {@link AuthenticationProperties}.
 * </p>
 *
 * @author: LinhLH
 * @see AuthenticationProperties
 * @see CorsFilter
 * @see WebMvcConfigurer
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfiguration {

    /**
     * Provides access to authentication-related properties, including CORS settings.
     * Injected via constructor by Lombok.
     */
    private final AuthenticationProperties ap;

    private final CorsConfiguration corsConfiguration;


    /**
     * Creates the source configuration for the {@link CorsFilter}.
     * <p>
     * Reads CORS settings (allowed origins, methods, headers, etc.) from {@link AuthenticationProperties}
     * and builds a {@link CorsConfiguration} object. This configuration is registered for all paths
     * ({@link SecurityConstants.UrlPattern#ALL}) only if allowed origins are defined.
     * </p>
     *
     * @return A {@link CorsConfigurationSource} to be used by the {@link CorsFilter}.
     * @see UrlBasedCorsConfigurationSource
     * @see CorsConfiguration
     * @see AuthenticationProperties#getCors()
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Register the configuration only if origins are specified
        if (corsConfiguration.getAllowedOriginPatterns() != null && !corsConfiguration.getAllowedOriginPatterns().isEmpty()) {
            _log.info("[corsConfigurationSource - Spring MVC] Registering CORS configuration for pattern: {}",
                    SecurityConstants.UrlPattern.ALL);
            source.registerCorsConfiguration(SecurityConstants.UrlPattern.ALL, corsConfiguration);
        } else {
            _log.warn("[corsConfigurationSource - Spring MVC] CORS filter registration skipped: No allowed origins configured.");
        }

        return source;
    }

    /**
     * Creates the primary {@link CorsFilter} bean.
     * <p>
     * This filter intercepts incoming requests early in the filter chain to handle
     * CORS preflight and actual requests based on the configuration provided by
     * {@link #corsConfigurationSource()}.
     * </p>
     *
     * @return A configured {@link CorsFilter}.
     * @see #corsConfigurationSource()
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    /**
     * Provides a {@link WebMvcConfigurer} bean for customizing Spring Web MVC.
     * <p>
     * This configurer is used here to:
     * <ol>
     *     <li>Define CORS mappings via {@link CorsRegistry}, offering an alternative or supplementary
     *         approach to the {@link CorsFilter}.</li>
     * </ol>
     * </p>
     *
     * @return A {@link WebMvcConfigurer} instance.
     * @see WebMvcConfigurer#addCorsMappings(CorsRegistry)
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configures CORS mappings at the Spring MVC level.
             * <p>
             * Applies CORS settings read from {@link AuthenticationProperties} to all
             * request paths ({@link SecurityConstants.UrlPattern#ALL}).
             * </p>
             * @param registry The registry to add CORS configurations to. Must be non-null.
             */
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                _log.info("Adding CORS mappings via WebMvcConfigurer for pattern: {}", SecurityConstants.UrlPattern.ALL);

                registry.addMapping(SecurityConstants.UrlPattern.ALL)
                        .exposedHeaders(StringUtil.split(ap.getCors().getExposedHeaders(), StringPool.COMMA))
                        .allowedOriginPatterns(StringUtil.split(ap.getCors().getAllowedOrigins(), StringPool.COMMA))
                        .allowedHeaders(StringUtil.split(ap.getCors().getAllowedHeaders(), StringPool.COMMA))
                        .allowedMethods(StringUtil.split(ap.getCors().getAllowedMethods(), StringPool.COMMA)) // Requires array/varargs
                        .maxAge(ap.getCors().getMaxAge());
                // Note: allowCredentials needs careful handling here, often better managed by CorsFilter
            }
        };
    }

    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }

    @Bean
    public ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }
}