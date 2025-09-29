package org.mbg.common.base.configuration;

import feign.RequestInterceptor;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfiguration {
    @Bean
    public RequestInterceptor authForwardingInterceptor() {
        return template -> {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                String auth = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                String user = attrs.getRequest().getHeader(SecurityConstants.Header.USER);
                String permission = attrs.getRequest().getHeader(SecurityConstants.Header.X_SERVICE_PERMISSIONS);
                if (auth != null && !auth.isBlank()) {
                    template.header(HttpHeaders.AUTHORIZATION, auth);
                    template.header(SecurityConstants.Header.USER, user);
                    template.header(SecurityConstants.Header.X_SERVICE_PERMISSIONS, permission);
                }
            }
        };
    }
}
