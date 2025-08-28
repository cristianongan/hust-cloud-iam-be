package org.mbg.common.cache.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to caching mechanisms.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize cache-related constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstants {

    /**
     * Miscellaneous cache-related constants.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Others {
        public static final String DEFAULT = "default";
    }

    /**
     * Constants representing SpEL (Spring Expression Language) expressions commonly used in caching annotations.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Expression {
        public static final String KEY = "#key";
        public static final String RESULT_EQUAL_NULL = "#result == null";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Package {
        public static final String DEFAULT = "com.vmc";
    }
}