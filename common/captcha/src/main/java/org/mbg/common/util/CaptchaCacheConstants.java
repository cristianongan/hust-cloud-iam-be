package org.mbg.common.util;

import org.mbg.common.cache.util.CacheConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants specifically for Captcha-related caching.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * <p>
 * <b>Important:</b> This class does NOT extend {@link CacheConstants} because a class
 * cannot extend an interface to inherit constants directly into its namespace.
 * class name (e.g., {@code CacheConstants.Expression.KEY}).
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CaptchaCacheConstants {

    /**
     * Constants for Captcha cache keys or related identifiers.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Others {
        public static final String CAPTCHA = "captcha";
        public static final String LOGIN_FAILED = "login-failed";
    }
}