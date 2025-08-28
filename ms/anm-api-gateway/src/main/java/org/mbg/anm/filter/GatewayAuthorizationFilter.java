package org.mbg.anm.filter;

import com.google.gson.Gson;
import org.mbg.common.keycloak.util.KeycloakConstants;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.HMACUtil;
import org.mbg.common.util.StringPool;
import org.mbg.anm.util.GatewayConstant;
import org.mbg.anm.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A Spring Cloud Gateway GlobalFilter that implements the "Gateway Enrichment" pattern.
 * <p>
 * It runs after successful JWT authentication (handled by Spring Security's
 * OAuth2 Resource Server filters) and extracts user roles/permissions
 * specific to the target downstream microservice from the JWT claims. It assumes
 * Keycloak Client Roles are used and mapped to the 'resource_access' claim in the JWT.
 * </p>
 * <p>
 * The filter then adds these service-specific permissions and the user ID to custom
 * request headers (e.g., {@link SecurityConstants.Header#X_SERVICE_PERMISSIONS},
 * {@link SecurityConstants.Header#X_USER_ID}) before forwarding the request to the
 * downstream service. This allows downstream services to perform fine-grained
 * authorization without needing to re-fetch permissions, relying on the information
 * provided by the trusted Gateway.
 * </p>
 * <p>
 * It relies on route metadata ('service-id') to identify the target service.
 * This filter does NOT implement the servlet-based AuthorizationFilter interface.
 * </p>
 *
 * @author: LinhLH
 * @see GlobalFilter
 * @see ReactiveSecurityContextHolder
 * @see ServerWebExchangeUtils#GATEWAY_ROUTE_ATTR
 */

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@RequiredArgsConstructor
public class GatewayAuthorizationFilter implements GlobalFilter {

    private final AuthenticationProperties ap;

    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    private final AuthzClient authzClient;

    /**
     * Filters the incoming request to enrich it with service-specific permissions
     * and user information before forwarding it to the downstream service.
     * <p>
     * This filter operates on requests that have successfully passed authentication
     * and have a matched route. It extracts relevant claims (like roles and permissions)
     * from the authenticated principal (JWT or OIDC user) based on the target service
     * identified by the route's metadata. These claims are then added as custom headers
     * to the request, allowing downstream services to perform authorization checks
     * without needing to re-process the authentication token.
     * </p>
     * <p>
     * If no authenticated principal or route is found, the filter chain proceeds
     * without enrichment. Errors during enrichment are logged, but the filter chain
     * still proceeds to avoid blocking the request entirely.
     * </p>
     *
     * @param exchange The current server exchange, providing access to request and response.
     * @param chain    The gateway filter chain to delegate to after processing.
     * @return A {@link Mono} that completes when the filter chain processing is done.
     */
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        RequestPath path = exchange.getRequest().getPath();

        LogUtil.info(_log, exchange, "[filter] Filter for request path {}", path);
        // Assumption: Spring Security's SecurityWebFilterChain (configured in GatewaySecurityConfiguration)
        // has already handled permitting public paths. If a request reaches this filter,
        // it's assumed to be a non-public path requiring authentication check and potential enrichment.

        // 1. Get Authentication (containing JWT) and the matched Route from the context
        Mono<Authentication> authenticationMono = ReactiveSecurityContextHolder.getContext()
                .doOnNext(context -> LogUtil.debug(_log, exchange, "[filter] Retrieved SecurityContext: {}", context))
                .doOnError(error -> LogUtil.warn(_log, exchange, "[filter] Error retrieving SecurityContext: {}", error.getMessage()))
                .map(SecurityContext::getAuthentication)
                // Ensure authentication exists, is authenticated
                .filter(auth -> auth != null
                        && auth.isAuthenticated())
                .doOnNext(auth -> LogUtil.debug(_log, exchange, "[filter] Successfully retrieved Authentication: {}", auth))
                .doOnError(error -> LogUtil.warn(_log, exchange, "[filter] Error retrieving Authentication: {}", error.getMessage()));

        Object gatewayRouteAttr = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

        LogUtil.debug(_log, exchange, "[filter] Retrieved gatewayRouteAttr: {}", gatewayRouteAttr);

        // Retrieve the Route object that Gateway determined for this request
        Mono<Route> routeMono = Optional.ofNullable(gatewayRouteAttr)
                .map(Route.class::cast)
                .map(Mono::just)
                .orElse(Mono.empty())
                .doOnNext(route -> LogUtil.debug(_log, exchange, "[filter] Successfully retrieved Route: {}", route))
                .doOnError(error -> LogUtil.warn(_log, exchange, "[filter] Error retrieving Route: {}", error.getMessage()));

        // 2. Combine Authentication and Route information. If both are present, enrich the request.
        Mono<Boolean> authAndRoutePresent = Mono.zip(
                        authenticationMono.hasElement(),
                        routeMono.hasElement())
                .map(tuple -> tuple.getT1() && tuple.getT2())
                .doOnNext(present -> LogUtil.debug(_log, exchange, "[filter] Both Authentication and Route present: {}", present));

        // 2. Combine Authentication and Route information. If both are present, enrich the request.
        return authAndRoutePresent.flatMap(present -> {
                    if (Boolean.TRUE.equals(present)) {
                        return Mono.zip(authenticationMono, routeMono)
                                .flatMap(tuple -> enrichRequestForService(tuple.getT1(),
                                        tuple.getT2(), exchange, chain));
                    } else {
                        LogUtil.trace(_log, exchange, "[filter] No authenticated JWT principal or route found for path: {}, " +
                                "proceeding without enrichment.", path);
                        return chain.filter(exchange);
                    }
                })

                // Handle any errors during the enrichment process gracefully
                .onErrorResume(ex -> {
                    LogUtil.error(_log, exchange, "[GatewayEnrichmentFilter] Error during request enrichment for path {}: {}",
                            path, ex.getMessage(), ex);

                    // Proceed the chain even if enrichment fails to avoid blocking the request
                    return chain.filter(exchange);
                });
    }

    /**
     * Enriches the incoming request with additional headers or metadata based on the authenticated user's principal
     * (either a JWT or an OIDC user) and the specified route. Additionally, forwards the request with the updated
     * information in the filter chain.
     *
     * @param authentication the current authentication object containing user details or token
     * @param route          the resolved route for the incoming request
     * @param exchange       the current server web exchange containing the HTTP request and response
     * @param chain          the gateway filter chain to pass the modified request through
     * @return a {@link Mono} that completes when the chain is processed
     */
    private Mono<Void> enrichRequestForService(Authentication authentication, Route route, ServerWebExchange exchange,
                                               GatewayFilterChain chain) {
        Object principal = authentication.getPrincipal();
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

        switch (principal) {
            case Jwt jwt -> {
                enrichRequestForJwt(exchange, jwt, route, requestBuilder);

                return defaultChain(exchange, chain, jwt.getSubject(), requestBuilder);
            }

            case DefaultOidcUser oidcUser -> {
                if (authentication instanceof OAuth2AuthenticationToken oauth2Auth) {
                    return getAccessTokenForOidcUser(oauth2Auth, exchange)
                            .flatMap(accessToken -> {
                                enrichRequestWithOidcUser(exchange, oidcUser, route, requestBuilder, accessToken);

                                return Mono.just(true);
                            })
                            .defaultIfEmpty(false)
                            .flatMap(hasToken -> {
                                if (!hasToken) {
                                    LogUtil.warn(_log, exchange, "[enrichRequestForService] No token available for OIDC user");
                                }

                                return defaultChain(exchange, chain, oidcUser.getSubject(), requestBuilder);
                            })
                            .onErrorResume(ConnectException.class, e -> {
                                LogUtil.error(_log, exchange, "[enrichRequestForService] Connection error: {}", e.getMessage());

                                // Trả về lỗi ngay lập tức
                                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                                return exchange.getResponse().writeWith(
                                        Mono.just(exchange.getResponse().bufferFactory().wrap(
                                                "{\"message\":\"Unable to connect to service\"}".getBytes()
                                        ))
                                );
                            });
                } else {
                    LogUtil.warn(_log, exchange, "[enrichRequestForService] Authentication is not OAuth2AuthenticationToken");
                }
            }

            default -> LogUtil.warn(_log, exchange, "[enrichRequestForService] Principal is of an unexpected type: {}",
                    principal.getClass().getName());
        }

        return chain.filter(exchange);
    }

    /**
     * Continues the filter chain with the potentially modified request.
     *
     * @param exchange       The current server web exchange.
     * @param chain          The gateway filter chain.
     * @param userId         The user ID to add to the header.
     * @param requestBuilder The request builder with potential modifications.
     * @return A {@link Mono} that completes when the chain is processed.
     */
    private Mono<Void> defaultChain(ServerWebExchange exchange, GatewayFilterChain chain, String userId,
                                    ServerHttpRequest.Builder requestBuilder) {
        if (userId != null) {
            requestBuilder.header(SecurityConstants.Header.X_USER_ID, userId);

            LogUtil.trace(_log, exchange, "[defaultChain] Adding header '{}': {}", SecurityConstants.Header.X_USER_ID, userId);
        }

        ServerHttpRequest mutatedRequest = requestBuilder.build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange);
    }

    /**
     * Retrieves the access token for an authenticated OIDC user.
     *
     * @param oauth2Auth The OAuth2 authentication token.
     * @param exchange   The current server web exchange.
     */
    private Mono<String> getAccessTokenForOidcUser(OAuth2AuthenticationToken oauth2Auth, ServerWebExchange exchange) {
        return authorizedClientRepository.loadAuthorizedClient(oauth2Auth.getAuthorizedClientRegistrationId(),
                oauth2Auth, exchange).map(client -> client.getAccessToken().getTokenValue());
    }

    /**
     * Enriches the server request with additional headers containing permissions
     * extracted from the provided JWT for the target service identified by the route.
     * If the target service ID cannot be determined from the route, a warning is logged
     * and the method exits without modifying the request.
     *
     * @param jwt            the JWT containing claims and permissions for the authenticated user
     * @param route          the route metadata for the current request, used to determine the target service ID
     * @param requestBuilder the mutable builder for the server HTTP request, used to add enriched headers
     */
    private void enrichRequestForJwt(ServerWebExchange exchange, Jwt jwt, Route route,
                                     ServerHttpRequest.Builder requestBuilder) {
        String targetServiceId = getTargetServiceId(route);

        if (targetServiceId == null) {
            LogUtil.warn(_log, exchange, "[enrichRequestForJwt] Missing 'service-id' metadata for route: {}. " +
                    "Cannot extract service-specific permissions from JWT.", route.getId());

            return;
        }

        LogUtil.trace(_log, exchange, "[enrichRequestForJwt] Enriching request with JWT permissions for service '{}', route '{}'",
                targetServiceId, route.getId());

        Map<String, Object> resourceAccess = jwt.getClaimAsMap(KeycloakConstants.JwtKey.RESOURCE_ACCESS);
        Map<String, Object> realmAccess = jwt.getClaimAsMap(KeycloakConstants.JwtKey.REALM_ACCESS);

        List<String> authorities = getAuthorities(exchange, realmAccess, resourceAccess, targetServiceId,
                jwt.getTokenValue());

        addPermissionToHeader(exchange, requestBuilder, authorities, targetServiceId);
    }

    /**
     * Retrieves a list of authorities (permissions and roles) for the given parameters by
     * combining service-specific permissions from 'resource_access' with realm-wide roles from 'realm_access'.
     * The resulting list may include both permissions associated with a specific service and roles
     * prefixed with a defined string.
     *
     * @param realmAccess     a map representing the 'realm_access' claim containing roles applicable across the realm
     * @param resourceAccess  a map representing the 'resource_access' claim containing service-specific permissions
     * @param targetServiceId the identifier of the target service to extract permissions for
     * @return a list of string authorities, combining service permissions and filtered realm roles
     */
    @SuppressWarnings("unchecked")
    private List<String> getAuthorities(
            ServerWebExchange exchange, Map<String, Object> realmAccess, Map<String, Object> resourceAccess,
            String targetServiceId, String accessToken) {
        LogUtil.info(_log, exchange, "[getAuthorities] Access token from Authentication: {}", accessToken);

        List<String> servicePermissions = extractPermissionsForService(exchange, resourceAccess, targetServiceId);
        List<String> roles = Optional.ofNullable(realmAccess)
                .map(r -> r.get(KeycloakConstants.JwtKey.ROLES))
                .map(r -> (List<String>) r)
                .orElse(Collections.emptyList())
                .stream()
                .map(String::toUpperCase)
                .filter(role -> role.startsWith(KeycloakConstants.JwtKey.ROLE_PREFIX))
                .toList();
        List<String> permissions = loadPermissionFromKeycloak(exchange, accessToken, targetServiceId);

        List<String> allAuthorities = new ArrayList<>();

        allAuthorities.addAll(servicePermissions);
        allAuthorities.addAll(roles);
        allAuthorities.addAll(permissions);

        return allAuthorities;
    }

    /**
     * Loads permissions from Keycloak using the provided access token and target service ID.
     *
     * @param exchange        The current server web exchange.
     * @param accessToken     The access token to use for authorization.
     * @param targetServiceId The ID of the target service.
     */
    private List<String> loadPermissionFromKeycloak(ServerWebExchange exchange, String accessToken, String targetServiceId) {
        AuthorizationRequest request = new AuthorizationRequest();

        request.setAudience(targetServiceId);

        AuthorizationResponse response = authzClient.authorization(accessToken).authorize(request);

        TokenIntrospectionResponse introspectionResponse = authzClient.protection()
                .introspectRequestingPartyToken(response.getToken());

        LogUtil.info(_log, exchange, "[loadPermissionFromKeycloak] introspectionResponse {}", new Gson().toJson(introspectionResponse));

        if (Boolean.FALSE.equals(introspectionResponse.getActive())) {
            LogUtil.warn(_log, exchange, "[loadPermissionFromKeycloak] RPT token is not active for token {}", accessToken);

            return new ArrayList<>();
        }

        return introspectionResponse.getPermissions()
                .stream()
                .flatMap(p -> {
                    String permissionName = p.getResourceName();
                    Set<String> scopes = p.getScopes();

                    return scopes.stream().map(s -> String.join(StringPool.COLON, permissionName, s));
                }).toList();
    }

    /**
     * Adds permissions to the header of the HTTP request being built. The permissions are
     * derived from the provided list of authorities and associated with the target service ID.
     * If the authorities list is empty, no headers are added and a debug log is generated
     * indicating the absence of specific permissions.
     *
     * @param requestBuilder  the mutable builder for the server HTTP request to add headers to
     * @param authorities     a list of permissions/authorities to be included in the request headers
     * @param targetServiceId the identifier of the target service for which permissions are being set
     */
    private void addPermissionToHeader(
            ServerWebExchange exchange, ServerHttpRequest.Builder requestBuilder, List<String> authorities,
            String targetServiceId) {
        if (!authorities.isEmpty()) {
            String permissionsHeaderValue = String.join(StringPool.COMMA, authorities).toUpperCase();

            requestBuilder.header(SecurityConstants.Header.X_SERVICE_PERMISSIONS, permissionsHeaderValue);
            requestBuilder.header(SecurityConstants.Header.X_SERVICE_PERMISSIONS_SIGNATURE,
                    HMACUtil.encodeHex(permissionsHeaderValue,
                            this.ap.getAuthentication().getSignature().getGatewaySharedSecret(),
                            HMACUtil.HMACSHA256));

            LogUtil.debug(_log, exchange, "[addPermissionToHeader] Adding header '{}' for service '{}': {} permissions",
                    SecurityConstants.Header.X_SERVICE_PERMISSIONS, targetServiceId, authorities.size());
        } else {
            LogUtil.debug(_log, exchange, "[addPermissionToHeader] No specific permissions found for service '{}'. " +
                            "Proceeding without '{}' header.",
                    targetServiceId, SecurityConstants.Header.X_SERVICE_PERMISSIONS);
        }
    }

    /**
     * Retrieves the target service ID from the metadata of the given route. The service ID
     * is identified by the key 'service-id' in the route's metadata map. If the metadata
     * or service ID is null, the method returns null.
     *
     * @param route the route whose metadata contains the target service ID
     * @return the target service ID as a string, or null if it is not present in the metadata
     */
    private String getTargetServiceId(Route route) {
        return Optional.ofNullable(route.getMetadata().get(GatewayConstant.Metadata.SERVICE_ID))
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * Enriches the HTTP request with OIDC user information, including permissions and roles,
     * based on the target service ID identified from the route. The enriched data is added
     * to the request headers, enabling further downstream processing.
     * If the target service ID cannot be resolved from the route metadata, a warning is logged
     * and the method exits without modifying the request.
     *
     * @param oidcUser       the authenticated OIDC user containing claims and roles to be processed
     * @param route          the resolved route for the current request, used to determine the target service ID
     * @param requestBuilder the mutable builder for the server HTTP request, used to add enriched headers
     */
    @SuppressWarnings("unchecked")
    private void enrichRequestWithOidcUser(
            ServerWebExchange exchange, DefaultOidcUser oidcUser, Route route, ServerHttpRequest.Builder requestBuilder,
            String accessToken) {
        String targetServiceId = getTargetServiceId(route);

        if (targetServiceId == null) {
            LogUtil.warn(_log, exchange, "[enrichRequestWithOidcUser] Missing 'service-id' metadata for route: {}. " +
                    "Cannot extract service-specific permissions from OidcUser.", route.getId());

            return;
        }

        LogUtil.trace(_log, exchange, "[enrichRequestWithOidcUser] Enriching request with OidcUser information for service '{}', route '{}'",
                targetServiceId, route.getId());

        Map<String, Object> claims = Optional.ofNullable(oidcUser.getClaims())
                .orElse(oidcUser.getIdToken().getClaims());

        // Example: You might want to forward roles or specific claims as headers
        Map<String, Object> resourceAccess =
                (Map<String, Object>) claims.get(KeycloakConstants.JwtKey.RESOURCE_ACCESS);
        Map<String, Object> realmAccess =
                (Map<String, Object>) claims.get(KeycloakConstants.JwtKey.REALM_ACCESS);

        List<String> authorities = getAuthorities(exchange, realmAccess, resourceAccess, targetServiceId, accessToken);

        addPermissionToHeader(exchange, requestBuilder, authorities, targetServiceId);
    }

    /**
     * Extracts a list of permissions (roles) for a specific service from the provided 'resource_access' claim.
     * If the claim is missing, the target service is not present, or an error occurs during extraction,
     * an empty list is returned.
     *
     * @param resourceAccess  a map representing the 'resource_access' claim, which contains details about
     *                        permissions for various services
     * @param targetServiceId the identifier of the target service whose permissions are to be extracted
     * @return a list of permissions (roles) for the specified service, or an empty list if the permissions
     * cannot be extracted
     */
    @SuppressWarnings("unchecked")
    private List<String> extractPermissionsForService(ServerWebExchange exchange, Map<String, Object> resourceAccess,
                                                      String targetServiceId) {
        // Check if the claim exists and contains an entry for our target service
        if (resourceAccess == null || !resourceAccess.containsKey(targetServiceId)) {
            LogUtil.trace(_log, exchange, "[extractPermissionsForService] JWT claim 'resource_access' is missing or does not contain " +
                    "key for service: {}", targetServiceId);

            return Collections.emptyList();
        }

        try {
            // Get the specific map for the target service
            Map<String, Object> serviceAccess = (Map<String, Object>) resourceAccess.get(targetServiceId);

            // Check if this map contains the 'roles' list
            if (serviceAccess != null && serviceAccess.containsKey(KeycloakConstants.JwtKey.ROLES)) {
                List<String> roles = (List<String>) serviceAccess.get(KeycloakConstants.JwtKey.ROLES);
                // Return the roles list, or an empty list if it's null
                return Optional.ofNullable(roles)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(String::toUpperCase)
                        .filter(role -> role.startsWith(KeycloakConstants.JwtKey.ROLE_PREFIX))
                        .toList();
            } else {
                LogUtil.trace(_log, exchange, "[extractPermissionsForService] 'resource_access' for service '{}' does not contain 'roles' key.",
                        targetServiceId);
            }
        } catch (ClassCastException e) {
            // Log an error if the JWT structure is not as expected
            LogUtil.error(_log, exchange, "[extractPermissionsForService] Error casting 'resource_access' claims for service '{}'. " +
                    "Verify JWT structure. Error: {}", targetServiceId, e.getMessage());
        } catch (Exception e) {
            // Catch other potential exceptions during claim extraction
            LogUtil.error(_log, exchange, "[extractPermissionsForService] Unexpected error extracting permissions for service '{}'. " +
                    "Error: {}", targetServiceId, e.getMessage());
        }

        // Return empty list in case of errors or missing data
        return Collections.emptyList();
    }
}
