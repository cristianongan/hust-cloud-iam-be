package org.mbg.anm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.jwt.JwtProvider;
import org.mbg.anm.security.UserDetailServiceImpl;
import org.mbg.anm.security.UserPrincipal;
import org.mbg.common.api.enums.ClientResponseError;
import org.mbg.common.api.exception.ClientResponseException;
import org.mbg.common.security.filter.AuthorizationFilter;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

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
//                    throw new BadRequestException(LabelKey.ERROR_INVALID_TOKEN,
//                            User.class.getName(), LabelKey.ERROR_INVALID_TOKEN);
                    throw new ClientResponseException(ClientResponseError.INVALID_TOKEN);
                }

                UserPrincipal userPrincipal = (UserPrincipal) userDetailService.loadUserByClientId(data[0]);

                if (Validator.isNull(userPrincipal) ||
                        !Validator.equals(userPrincipal.getPassword(), data[1])) {
                    throw new ClientResponseException(ClientResponseError.UNAUTHORIZED);
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
