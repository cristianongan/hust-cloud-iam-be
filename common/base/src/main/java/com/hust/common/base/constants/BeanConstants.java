package com.hust.common.base.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to Spring Bean names or identifiers.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * @author: LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeanConstants {

    public static final String SPRING_SECURITY_AUDITOR_AWARE = "springSecurityAuditorAware";
}