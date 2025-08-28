package org.mbg.common.intergration.interceptor;

import org.mbg.common.security.util.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * A Feign request interceptor that propagates the JWT token from the incoming
 * request to the outgoing service-to-service requests. This ensures that
 * downstream services receive the necessary authentication credentials.
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {
    /**
     * This method is called for every request made by a Feign client.
     * It intercepts the request to add the Authorization header.
     *
     * @param template The request template that can be modified before the request is sent.
     */
    @Override
    public void apply(RequestTemplate template) {
        // Retrieve the current authentication object from the security context.
        // This context is thread-local and holds the security details of the original incoming request.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Proceed only if there is an authenticated user and the authentication is a JWT.
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            // Extract the raw token value from the authentication object.
            final String tokenValue = jwtAuth.getToken().getTokenValue();

            // Add the "Authorization: Bearer <token>" header to the outgoing Feign request.
            // This ensures the downstream service can authenticate and authorize this call.
            template.header(SecurityConstants.Header.AUTHORIZATION_HEADER,
                    String.format("%s%s", SecurityConstants.Header.BEARER_START, tokenValue));
        }
    }
}
