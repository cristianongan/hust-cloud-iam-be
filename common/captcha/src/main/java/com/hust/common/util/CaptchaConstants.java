package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to Captcha functionality.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize these constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CaptchaConstants {

    /**
     * Constants representing entity names related to Captcha.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EntityName {
        public static final String CAPTCHA = "captcha";
    }

    /**
     * Constants representing configuration property prefixes for Captcha.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PropertyPrefix {
        public static final String CAPTCHA_CONFIG = "captcha.config";
    }
}