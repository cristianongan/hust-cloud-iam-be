package com.hust.common.security.configuration;

import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.StringPool;
import com.hust.common.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = SecurityConstants.PropertyPrefix.SECURITY)
public class AuthenticationProperties {
    
    private Authentication authentication;
    
    private Cors cors;
    
    private Cache cache;
    
    private UserAgent userAgent;
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Authentication {
        private Jwt jwt;
        
        private Cookie cookie;

        private User user;

        private String[] publicUrlPatterns;

        private Signature signature;
        
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Jwt {
            
            private String base64Secret;
            
            private int tokenDuration;
            
            private int tokenRememberMeDuration;
            
            private int refreshTokenDuration;
            
            private int dataDuration;
            
            private int csrfTokenDuration;
        }
        
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Cookie {

            private String domainName;
            
            private boolean enableSsl;
            
            private String path;
            
            private boolean httpOnly;
            
            private String sameSite;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Signature {
            private String gatewaySharedSecret;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class User {
            private int loginMaxAttemptTime;

            private int passwordMaxAttemptTime;
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cors {
        private String allowedOrigins;
        
        private String allowedMethods;
        
        private String allowedHeaders;
        
        private String exposedHeaders;
        
        private boolean allowCredentials;
        
        private long maxAge;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cache {
        private String[] urlPatterns;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserAgent {
        private boolean enable;
        
        private String allowedAgent;
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        config.applyPermitDefaultValues();
        // Configure CORS based on properties
        config.setAllowedHeaders(
                Arrays.asList(StringUtil.split(this.getCors().getAllowedHeaders(), StringPool.COMMA)));
        config.setAllowedMethods(
                Arrays.asList(StringUtil.split(this.getCors().getAllowedMethods(), StringPool.COMMA)));
        config.setAllowedOriginPatterns(
                Arrays.asList(StringUtil.split(this.getCors().getAllowedOrigins(), StringPool.COMMA)));
        config.setExposedHeaders(
                Arrays.asList(StringUtil.split(this.getCors().getExposedHeaders(), StringPool.COMMA)));
        config.setAllowCredentials(this.getCors().isAllowCredentials());
        config.setMaxAge(this.getCors().getMaxAge());

        return config;
    }
}
