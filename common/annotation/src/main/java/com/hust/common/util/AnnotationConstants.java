package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants used for annotations in the application.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize these constants
 * based on their context or purpose.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnnotationConstants {

    /**
     * Constants representing entity names, often used in annotations like @Entity(name=...).
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EntityName {
        public static final String SIGNATURE = "signature";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Pointcut {
        public static final String CONTROLLER = "within(com.hust.anm.controller..*)";
        public static final String SERVICE = "within(com.hust.anm.service.impl..*)";
    }
}