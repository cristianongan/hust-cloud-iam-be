package com.hust.common.base.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.common.security.filter.AuthorizationFilter;
import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.Validator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ServiceSecurityFilter implements AuthorizationFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String user = request.getHeader(SecurityConstants.Header.USER);
        String permission = request.getHeader(SecurityConstants.Header.X_SERVICE_PERMISSIONS);

        if (Validator.isNotNull(user)) {

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (Validator.isNotNull(permission)) {
                authorities = Arrays.stream(permission.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, "N/A",
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
