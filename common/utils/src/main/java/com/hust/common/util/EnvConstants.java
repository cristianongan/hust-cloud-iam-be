package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to application environments,
 * profiles, properties, and protocols.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize these constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvConstants {

    /**
     * Constants representing Spring profile names.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Profile {
        public static final String DEVELOPMENT = "dev";
        public static final String PRODUCTION = "prod";
        public static final String QA = "qa";
        /**
         * Represents the property key for the default Spring profile, not a profile name itself.
         */
        public static final String DEFAULT = "spring.profiles.default";
    }

    /**
     * Constants representing common application property keys.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Properties {
        public static final String SERVER_PORT = "server.port";
        public static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
        public static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
        public static final String SPRING_APPLICATION_NAME = "spring.application.name";
    }

    /**
     * Constants representing network protocols.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Protocol {
        public static final String HTTP = "http";
        public static final String HTTPS = "https";
    }
}