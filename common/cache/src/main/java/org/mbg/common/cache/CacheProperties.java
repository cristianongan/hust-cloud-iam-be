package org.mbg.common.cache;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheProperties {

    private Map<String, Integer> timeToLives;

    private String configType;

    private RedisProperties redis;

    public enum Mode {
        STANDALONE, SENTINEL
    }

    @Data
    public static class RedisProperties {
        private String mode;

        private int cacheDuration;

        private Standalone standalone;

        private Sentinel sentinel;

        private LettucePool lettucePool;

        @Getter
        @Setter
        public static class Standalone {

            private int port;

            private String password;

            private String host;
        }

        @Getter
        @Setter
        public static class Sentinel {

            private int port;

            private String password;

            private String master;

            private List<String> nodes;
        }

        @Getter
        @Setter
        public static class LettucePool {

            private long shutdownTimeout;

            private long commandTimeout;

            private int minIdle;

            private int maxIdle;

            private long maxWaitMillis;

            private int maxTotal;
        }


    }
}
