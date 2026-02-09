package com.hust.iam.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.jwt.JwtProvider;
import com.hust.iam.security.UserDetailServiceImpl;
import com.hust.iam.security.UserPrincipal;
import com.hust.common.security.filter.AuthorizationFilter;
import com.hust.common.security.util.SecurityConstants;
import com.hust.common.security.util.SecurityUtils;
import com.hust.common.util.Validator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serial;

@Slf4j
@RequiredArgsConstructor
public class UserSecurityFilter implements AuthorizationFilter {

    private final JwtProvider jwtProvider;

    private final UserDetailServiceImpl  userDetailService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String auth = request.getHeader("Authorization");
        String token = null;
        Authentication authentication = null;
        if (StringUtils.hasText(auth) && auth.startsWith(SecurityConstants.Header.BEARER_START)) {
            token = auth.substring(SecurityConstants.Header.BEARER_START.length());

            if (Validator.isNotNull(token)) {
                this.jwtProvider.validateToken(token);

                authentication = this.jwtProvider.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        if (StringUtils.hasText(auth) && auth.startsWith(SecurityConstants.Header.BASIC_START)) {

            if (Validator.isNotNull(auth)) {
                String[] data = SecurityUtils.getBasicAuthentication(auth);

                if (Validator.isNull(data) || data.length != 2) {
                    throw new org.springframework.security.core.AuthenticationException("Invalid token"){
                        @Serial
                        private static final long serialVersionUID = -5340213868525396528L;
                    };
                }

                UserPrincipal userPrincipal = (UserPrincipal) userDetailService.loadUserByClientId(data[0]);

                if (Validator.isNull(userPrincipal) ||
                        !Validator.equals(userPrincipal.getPassword(), data[1])) {
                    throw new org.springframework.security.access.AccessDeniedException("No permission");
                }

                authentication = new UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.getAuthorities());

            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.Header.BEARER_START)) {
            return token.substring(SecurityConstants.Header.BEARER_START.length());
        }

        return null;
    }
}
