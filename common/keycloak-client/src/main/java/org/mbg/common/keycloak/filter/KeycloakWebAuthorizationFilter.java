package org.mbg.common.keycloak.filter;

import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.HMACUtil;
import org.mbg.common.util.StringPool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Spring Security filter that verifies permissions passed via request headers using an HMAC signature.
 * <p>
 * This filter is designed to run after initial authentication (e.g., JWT validation by Spring Security's
 * OAuth2 Resource Server support). It expects specific headers, typically added by a trusted upstream
 * service like an API Gateway:
 * <ul>
 *     <li>{@link SecurityConstants.Header#X_SERVICE_PERMISSIONS}: Contains a comma-separated list of permissions/roles.</li>
 *     <li>{@link SecurityConstants.Header#X_SERVICE_PERMISSIONS_SIGNATURE}: Contains the HMAC-SHA256 signature
 *         of the permissions header value, calculated using a shared secret.</li>
 * </ul>
 * The filter retrieves the shared secret from {@link AuthenticationProperties}. It uses the {@link HMACUtil}
 * utility class to calculate the expected signature and compare it securely against the received signature.
 * </p>
 * <p>
 * If the signature is valid, the filter parses the permissions from the header, converts them into
 * {@link SimpleGrantedAuthority} objects, and updates the {@link SecurityContextHolder} with a new
 * {@link UsernamePasswordAuthenticationToken} containing the original principal and the verified authorities.
 * </p>
 * <p>
 * If the permissions header is present but the signature header is missing, or if the signature is invalid,
 * an {@link UnauthorizedException} is thrown, effectively denying the request.
 * </p>
 * <p>
 * **Security Note:** This filter relies on the confidentiality of the shared secret key configured in
 * {@link AuthenticationProperties} and the assumption that the upstream service (Gateway) is trusted.
 * Network isolation preventing direct access to the microservice is still highly recommended.
 * </p>
 *
 * @author: LinhLH (Updated with Signature Verification)
 * @see AuthorizationFilter
 * @see AuthenticationProperties
 * @see HMACUtil
 * @see SecurityContextHolder
 * @see SecurityConstants.Header
 */
@Slf4j // Lombok annotation to generate an SLF4J logger instance named 'log'
@Component // Marks this class as a Spring component, eligible for auto-detection
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required final fields
public class KeycloakWebAuthorizationFilter extends OncePerRequestFilter implements AuthorizationFilter {

    /**
     * Configuration properties related to general authentication settings,
     * including the shared secret for signature verification.
     */
    private final AuthenticationProperties ap;

    /**
     * Performs the core filtering logic: verifies header signature and updates security context.
     * <p>
     * This method is executed once per request. It checks for the presence and validity
     * of the permission and signature headers. If valid, it updates the security context;
     * otherwise, it throws an {@link UnauthorizedException}.
     * </p>
     *
     * @param request     The incoming servlet request.
     * @param response    The outgoing servlet response.
     * @param filterChain The filter chain to proceed with.
     * @throws ServletException      If a servlet-related error occurs during filtering.
     * @throws IOException           If an I/O error occurs during filtering.
     * @throws UnauthorizedException If the signature is missing or invalid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        _log.info("[KeycloakWebAuthorizationFilter] Received request for path: {}", request.getServletPath());

        // Retrieve the permission and signature headers from the request
        final String permissionsHeaderValue = request.getHeader(SecurityConstants.Header.X_SERVICE_PERMISSIONS);
        final String receivedSignatureHex = request.getHeader(SecurityConstants.Header.X_SERVICE_PERMISSIONS_SIGNATURE);
        // Get the authentication object potentially set by previous filters (e.g., JWT validation)
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();

        // If there's no prior authentication, this filter cannot proceed meaningfully.
        if (Objects.isNull(originalAuthentication)) {
            _log.debug("[KeycloakWebAuthorizationFilter] Skipping KeycloakWebAuthorizationFilter for {} because " +
                    "authentication is null.", request.getServletPath());

            filterChain.doFilter(request, response);

            return; // Stop processing in this filter
        }

        // Check if the permissions header is present
        if (StringUtils.hasText(permissionsHeaderValue)) {
            _log.debug("[KeycloakWebAuthorizationFilter] Found header '{}': '{}'", 
                    SecurityConstants.Header.X_SERVICE_PERMISSIONS, permissionsHeaderValue);

            // If the permissions header exists, the signature header MUST also exist.
            if (!StringUtils.hasText(receivedSignatureHex)) {
                _log.warn("[KeycloakWebAuthorizationFilter] Missing signature header '{}' while '{}' is present. Rejecting request.",
                        SecurityConstants.Header.X_SERVICE_PERMISSIONS_SIGNATURE, SecurityConstants.Header.X_SERVICE_PERMISSIONS);
                // Throw an exception to deny access
                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_UNAUTHORIZED));
            }

            // Validate the signature using the helper method
            if (isValidSignature(permissionsHeaderValue, receivedSignatureHex)) {
                _log.debug("[KeycloakWebAuthorizationFilter] Signature for permissions header is VALID.");

                try {
                    // Parse the comma-separated permissions from the header value
                    List<String> permissions = parsePermissionsHeader(permissionsHeaderValue);

                    // Convert the permission strings into Spring Security authorities.
                    // Note: Currently, no "ROLE_" prefix is added here. Ensure consistency with downstream usage.
                    List<SimpleGrantedAuthority> authorities = permissions.stream()
                            .map(p -> new SimpleGrantedAuthority(p.toUpperCase())) // Convert to uppercase authority
                            .collect(Collectors.toList());

                    // Get the original principal (e.g., the Jwt object) from the existing authentication
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    // Create a new Authentication token containing the original principal and the NEWLY verified authorities
                    UsernamePasswordAuthenticationToken updatedAuthentication =
                            new UsernamePasswordAuthenticationToken(principal, null, authorities); // Credentials set to null

                    // Update the SecurityContext with the new authentication object
                    SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

                    _log.info("[KeycloakWebAuthorizationFilter] Updated SecurityContext using verified permissions " +
                                    "from header. Principal: {}, Authorities: {}", getPrincipalName(principal), authorities);

                } catch (Exception e) {
                    // Log any error during permission parsing or context update
                    _log.error("[KeycloakWebAuthorizationFilter] Error processing verified permissions header '{}': {}",
                            permissionsHeaderValue, e.getMessage(), e);
                    // Rethrow as UnauthorizedException to deny access
                    throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_UNAUTHORIZED), e);
                }
            } else {
                // Signature was provided but is invalid
                _log.warn("[KeycloakWebAuthorizationFilter] INVALID signature for permissions header '{}'. " +
                                "Received signature: '{}'. Rejecting request.", permissionsHeaderValue, receivedSignatureHex);
                // Throw an exception to deny access
                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_UNAUTHORIZED));
            }
        } else {
            // Permissions header was not found, skip signature verification and enrichment.
            _log.trace("[KeycloakWebAuthorizationFilter] No '{}' header found, skipping signature verification and permission enrichment.",
                    SecurityConstants.Header.X_SERVICE_PERMISSIONS);

            // Log if proceeding with the original authentication
            if (originalAuthentication.isAuthenticated()) {
                _log.trace("[KeycloakWebAuthorizationFilter] Proceeding with original authentication for principal: {}",
                        getPrincipalName(originalAuthentication.getPrincipal()));
            }
        }

        // Always continue the filter chain, allowing subsequent filters to run.
        // If an UnauthorizedException was thrown earlier, this line won't be reached for that request.
        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether this filter should be skipped based on the request path.
     * <p>
     * This method prevents the filter from running for paths considered public,
     * such as actuator endpoints or static resources, improving performance and
     * avoiding unnecessary processing.
     * </p>
     *
     * @param request The current servlet request.
     * @return {@code true} if the filter should not be applied (request path is public),
     * {@code false} otherwise.
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // Delegate the check to the isPublicPath helper method
        boolean shouldNotFilter = isPublicPath(request.getServletPath());

        if (shouldNotFilter) {
            _log.trace("[KeycloakWebAuthorizationFilter] Skipping KeycloakAuthorizationFilter for public path: {}", request.getServletPath());
        }

        return shouldNotFilter;
    }

    /**
     * Validates the received HMAC signature against a calculated signature for the given data.
     * <p>
     * It uses the shared secret configured in {@link AuthenticationProperties} and delegates
     * the cryptographic operations (hashing, hex decoding, secure comparison) to {@link HMACUtil}.
     * </p>
     *
     * @param data                 The data string that was signed (value of the permissions header).
     * @param receivedSignatureHex The signature received from the request header (expected in Hex format).
     * @return {@code true} if the received signature matches the calculated signature, {@code false} otherwise.
     */
    private boolean isValidSignature(String data, String receivedSignatureHex) {
        try {
            // Calculate the expected signature using HMACUtil
            byte[] calculatedSignatureBytes = HMACUtil.hashToBytes(
                    data,
                    this.ap.getAuthentication().getSignature().getGatewaySharedSecret(), // Retrieve shared secret
                    HMACUtil.HMACSHA256 // Specify the algorithm
            );

            // Decode the received hex signature string into bytes using HMACUtil
            byte[] receivedSignatureBytes = HMACUtil.decodeHex(receivedSignatureHex);

            // Perform a constant-time comparison of the byte arrays using HMACUtil
            boolean isSignatureValid = HMACUtil.isSignatureValid(calculatedSignatureBytes, receivedSignatureBytes);

            // Log if the signature doesn't match, including the calculated signature for debugging
            if (!isSignatureValid) {
                _log.warn("[KeycloakWebAuthorizationFilter] Signature mismatch. Calculated (Hex): {}",
                        HMACUtil.encodeHex(calculatedSignatureBytes)); // Encode calculated bytes to hex for logging
            }

            return isSignatureValid;

        } catch (Exception e) {
            // Log any exception during signature calculation, decoding, or comparison
            _log.error("[KeycloakWebAuthorizationFilter] Error decoding received signature hex '{}' or during HMAC verification: {}",
                    receivedSignatureHex, e.getMessage(), e); // Include exception details
            // Treat any error during verification as an invalid signature
            return false;
        }
    }

    /**
     * Parses the permissions header string into a list of individual permission strings.
     * <p>
     * Assumes the header value is a comma-separated string. It trims whitespace
     * from each permission and filters out any empty entries.
     * </p>
     *
     * @param headerValue The raw string value from the permissions header.
     * @return A {@link List} of permission strings, or an empty list if the header is null or blank.
     */
    private List<String> parsePermissionsHeader(String headerValue) {
        // Return empty list if the header value is effectively empty
        if (!StringUtils.hasText(headerValue)) {
            return Collections.emptyList();
        }

        // Split the string by comma, trim whitespace, filter out empty strings, and collect into a list
        return Arrays.stream(headerValue.split(StringPool.COMMA)) // Split by comma
                .map(String::trim) // Trim whitespace from each part
                .filter(p -> !p.isEmpty()) // Remove any empty parts resulting from split/trim
                .toList(); // Collect the results into an immutable list (Java 16+)
        // Use .collect(Collectors.toList()) for older Java versions
    }

    /**
     * Safely extracts a user identifier (name or subject) from the principal object.
     * <p>
     * Handles cases where the principal is a simple {@link String} or a {@link Jwt} object.
     * Returns a blank string if the principal is null or cannot be interpreted.
     * </p>
     *
     * @param principal The principal object from the {@link Authentication}.
     * @return The user identifier string (e.g., username or JWT subject), or a blank string if unavailable.
     */
    private String getPrincipalName(Object principal) {
        // If the principal is already a String, return it directly
        if (principal instanceof String name) { // Use pattern matching for instanceof (Java 16+)
            return name;
        }

        // If the principal is a Jwt object, return its subject claim
        if (principal instanceof Jwt jwt) { // Use pattern matching for instanceof (Java 16+)
            return jwt.getSubject();
        }

        // Return a blank string if the principal is null or of an unexpected type
        return StringPool.BLANK;
    }
}