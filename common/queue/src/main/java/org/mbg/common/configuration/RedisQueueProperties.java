package org.mbg.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "redis-queue")
@Getter
@Setter
public class RedisQueueProperties {

    private List<String> priorityTopics;

    private List<String> normalTopics;
}
