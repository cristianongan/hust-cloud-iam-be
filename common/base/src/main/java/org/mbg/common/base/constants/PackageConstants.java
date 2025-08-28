package org.mbg.common.base.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants related to package names or structures.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * @author: LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PackageConstants {

    public static final String DEFAULT_JPA_REPOSITORY = "com.vmc.repository";

    public static final String DEFAULT_PACKAGE_SCAN = "com.vmc";

    public static final String PACKAGE_CACHE = "com.vmc.common.cache";

    public static final String PACKAGE_MES = "com.vmc.mes";

    public static final String PACKAGE_LABEL = "com.vmc.common.label";

}