package com.hust.common.base.configuration;

import com.hust.common.base.constants.PropertyConstants;
import com.hust.common.util.EnvConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no {@code spring.profiles.active} set in the environment or as command line argument.
 * If the value is not available in {@code application.yml} then {@code dev} profile will be used as default.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultProfileUtil {
    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application.
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the application.yml file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(PropertyConstants.SPRING_PROFILE_DEFAULT, EnvConstants.Profile.DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}
