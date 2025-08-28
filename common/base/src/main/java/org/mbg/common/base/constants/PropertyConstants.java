package org.mbg.common.base.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to application property names or keys.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * @author: LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyConstants {

    // Constants defined in interfaces are implicitly public static final.
    // We make it explicit in the class.
    public static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default"; // Hằng số này đã được chuyển đổi
}