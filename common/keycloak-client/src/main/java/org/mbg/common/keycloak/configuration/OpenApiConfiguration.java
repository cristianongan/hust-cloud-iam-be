package org.mbg.common.keycloak.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Centralized OpenAPI configuration for enabling OAuth2 authentication
 * in Swagger UI across all microservices.
 * <p>
 * This configuration defines a security scheme for Keycloak (OAuth2/OIDC)
 * and applies it globally to all endpoints.
 */
@Configuration
// 1. Define general API information and apply a global security requirement.
//    This ensures that the "Authorize" button appears and that all endpoints
//    are marked as protected by default.
@OpenAPIDefinition(
        info = @Info(
                title = "API", // This title can be overridden in each service if needed
                version = "1.0",
                description = "This document describes the API endpoints."
        ),
        security = {
                @SecurityRequirement(name = "keycloak_auth")
        }
)
// 2. Define the details of the security scheme itself.
@SecurityScheme(
        name = "keycloak_auth", // A logical name used to reference this scheme.
        type = SecuritySchemeType.OAUTH2, // Specifies the security type as OAuth2.
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER, // The token is expected in the Authorization header.
        // 3. Configure the specific OAuth2 flows.
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        // These URLs are dynamically injected from application properties,
                        // making this configuration reusable across different environments.
                        authorizationUrl = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/auth",
                        tokenUrl = "${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "Standard OpenID Connect scope"),
                                @OAuthScope(name = "profile", description = "Grants access to user profile information"),
                                @OAuthScope(name = "email", description = "Grants access to user's email address"),
                                @OAuthScope(name = "roles", description = "Grants access to user roles")
                        }
                )
        )
)
public class OpenApiConfiguration {
    // This class is used for configuration purposes only.
    // The annotations on the class provide all the necessary setup for SpringDoc.
}