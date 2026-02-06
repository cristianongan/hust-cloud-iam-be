package com.hust.common.base.filter;

import lombok.extern.slf4j.Slf4j;
import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.StringUtil;
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
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author: LinhLH
 **/
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Run this filter very early
public class GlobalRequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);


        String requestId = request.getHeader(SecurityConstants.Header.X_REQUEST_ID);

        if (StringUtil.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        response.setHeader(SecurityConstants.Header.X_REQUEST_ID, requestId);

        MDC.put(SecurityConstants.Header.REQUEST_ID_MDC, requestId);

        try {
            // Log request before proceeding
            // log.info("Incoming Request: {} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
            filterChain.doFilter(req, res); // Continue chain
        } finally {
            // Log response after chain
            // log.info("Outgoing Response: {} {} with status {}", request.getMethod(), request.getRequestURI(), response.getStatus());
            String reqBody = safeBody(req.getContentAsByteArray(), req.getContentType());
            String resBody = safeBody(res.getContentAsByteArray(), res.getContentType());
            long took = System.currentTimeMillis() - start;

            _log.info("request-id: {} - took: {} - payload : {} - response: {}",requestId, took, reqBody, resBody);
            MDC.remove(SecurityConstants.Header.REQUEST_ID_MDC);
            res.copyBodyToResponse();
        }
    }

    private String safeBody(byte[] bytes, String contentType) {
        if (contentType != null && (contentType.startsWith("multipart/") ||
                contentType.startsWith("application/octet-stream"))) {
            return "<binary>";
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        if (s.length() > 10_000) s = s.substring(0, 10_000) + "...(truncated)";
        return s;
    }
}
