package com.hust.common.security.filter;

import com.hust.common.security.util.SecurityConstants;
import jakarta.servlet.Filter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

/**
 * Defines a contract or provides utility methods related to authorization filtering.
 * <p>
 * Currently, this interface primarily offers a default method {@link #isPublicPath(String)}
 * to conveniently check if a given request path matches predefined public URL patterns,
 * typically used to bypass certain security checks for public endpoints.
 * </p>
 *
 * @author: LinhLH
 * @see SecurityConstants.PublicUrlPattern
 * @see AntPathMatcher
 */
public interface AuthorizationFilter extends Filter {

    /**
     * Checks if the given request path matches any of the defined public URL patterns.
     * <p>
     * This default implementation utilizes an {@link AntPathMatcher} to compare the
     * provided {@code requestPath} against the patterns obtained from
     * {@link SecurityConstants.PublicUrlPattern#getPublicUrlPattern()}.
     * </p>
     *
     * @param requestPath The incoming request path to check. Should not be null, although
     *                    the implementation handles null gracefully by returning false.
     * @return {@code true} if the {@code requestPath} matches any of the configured
     *         public URL patterns, {@code false} otherwise (including if the path is null
     *         or no patterns are defined).
     */
    default boolean isPublicPath(String requestPath) {
        // Use a local instance or inject if needed elsewhere
        final AntPathMatcher pathMatcher = new AntPathMatcher();

        // Retrieve the public URL patterns
        String[] publicPatterns = SecurityConstants.PublicUrlPattern.getPublicUrlPattern();

        // Gracefully handle null input path
        if (requestPath == null) {
            return false;
        }

        // Use Stream API's anyMatch for concise checking against all patterns
        return Arrays.stream(publicPatterns)
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
}