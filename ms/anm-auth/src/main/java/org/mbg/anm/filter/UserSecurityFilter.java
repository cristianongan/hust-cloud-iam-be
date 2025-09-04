package org.mbg.anm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class UserSecurityFilter implements AuthorizationFilter {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String auth = request.getHeader("Authorization");
        String token = this.resolveToken(auth);

         if (Validator.isNotNull(token)) {
             this.jwtProvider.validateToken(token);

             Authentication authentication =
                     this.jwtProvider.getAuthentication(token);

             SecurityContextHolder.getContext().setAuthentication(authentication);
         }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.Header.BEARER_START)) {
            return token.substring(SecurityConstants.Header.BEARER_START.length());
        }

        return null;
    }
}
