package org.mbg.common.keycloak.util;

import org.mbg.common.cache.util.CacheConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants specifically for Keycloak-related caching.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * <p>
 * <b>Important:</b> This class does NOT extend {@link CacheConstants} because a class
 * cannot extend an interface to inherit constants directly into its namespace.
 * Constants from {@link CacheConstants} must be accessed statically using the full
 * class name (e.g., {@code CacheConstants.Expression.KEY}).
 * </p>
 * @author: LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakCacheConstants {

    /**
     * Constants for Keycloak cache keys or related identifiers.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Others {
        public static final String USER_PERMISSION = "user-permission";
    }}