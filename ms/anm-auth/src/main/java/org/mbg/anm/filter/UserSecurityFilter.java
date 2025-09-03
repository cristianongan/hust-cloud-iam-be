package org.mbg.anm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class UserSecurityFilter implements AuthorizationFilter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String auth = request.getHeader("Authorization");
        String token = this.resolveToken(auth);


    }

    private String resolveToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.Header.BEARER_START)) {
            return token.substring(SecurityConstants.Header.BEARER_START.length());
        }

        return null;
    }
}
