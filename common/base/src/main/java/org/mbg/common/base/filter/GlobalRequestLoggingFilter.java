package org.mbg.common.base.filter;

import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.StringUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @author: LinhLH
 **/
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Run this filter very early
public class GlobalRequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestId = request.getHeader(SecurityConstants.Header.X_REQUEST_ID);

        if (StringUtil.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        response.setHeader(SecurityConstants.Header.X_REQUEST_ID, requestId);

        MDC.put(SecurityConstants.Header.REQUEST_ID_MDC, requestId);

        try {
            // Log request before proceeding
            // log.info("Incoming Request: {} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
            filterChain.doFilter(request, response); // Continue chain
        } finally {
            // Log response after chain
            // log.info("Outgoing Response: {} {} with status {}", request.getMethod(), request.getRequestURI(), response.getStatus());
            MDC.remove(SecurityConstants.Header.REQUEST_ID_MDC);
        }
    }
}
