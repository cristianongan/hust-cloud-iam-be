package org.mbg.common.keycloak.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to Keycloak integration.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize these constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakConstants {

    /**
     * Constants representing entity names related to Keycloak.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EntityName {
        public static final String KEYCLOAK = "keycloak";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class JwtKey {
        public static final String RESOURCE_ACCESS = "resource_access";
        public static final String REALM_ACCESS = "realm_access";
        public static final String ROLES = "roles";
        public static final String ROLE_PREFIX = "ROLE_";
    }
}