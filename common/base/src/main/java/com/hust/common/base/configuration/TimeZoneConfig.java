package com.hust.common.base.configuration;

import com.hust.common.base.constants.ConfigurationConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no {@code spring.profiles.active} set in the environment or as command line argument.
 * If the value is not available in {@code application.yml} then {@code dev} profile will be used as default.
 */

@Configuration
public class TimeZoneConfig {
    @Value(ConfigurationConstants.SPRING_APPLICATION_TIME_ZONE)
    String timeZone;

    @PostConstruct
    void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}
