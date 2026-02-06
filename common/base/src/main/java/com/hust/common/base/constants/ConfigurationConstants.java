package com.hust.common.base.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to application configuration properties.
 * These constants often represent placeholders resolved by the Spring environment.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * @author: LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationConstants {

    public static final String SPRING_APPLICATION_TIME_ZONE = "${spring.jackson.time-zone}";
}