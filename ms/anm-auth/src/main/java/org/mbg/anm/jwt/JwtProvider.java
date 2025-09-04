package org.mbg.anm.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.security.UserPrincipal;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.cache.CacheProperties;
import org.mbg.common.cache.util.CacheConstants;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.anm.service.TokenService;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.DateUtil;
import org.mbg.common.util.StringPool;
import org.mbg.common.util.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {
    private SecretKey key;

    private PublicKey publicKey;

    private final AuthenticationProperties authenticationProperties;

    private final CacheProperties cacheProperties;

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    private JwtParser jwtParser;

    private Map<String, Integer> timeToLives;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes;

        String secret = this.authenticationProperties.getAuthentication().getJwt().getBase64Secret();

        if (Validator.isNull(secret)) {
            throw new Exception("no secret found");
        } else {
            keyBytes = Base64.getDecoder().decode(secret);
        }

        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");

        this.jwtParser = Jwts.parser().verifyWith(key).build();

        this.timeToLives = this.cacheProperties.getTimeToLives();
    }

    public boolean validateToken(String token) {
        String username = StringPool.BLANK;

        try {
            Claims claims = this.jwtParser.parseSignedClaims(token).getPayload();
        } catch (
            MalformedJwtException ex) {
                _log.error("Access token malformed");

                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_TOKEN_MALFORMED));

        } catch (ExpiredJwtException ex) {
                _log.error("Access token has expired");

                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_TOKEN_HAS_EXPIRED));
        } catch (UnsupportedJwtException ex) {
                _log.error("Unsupported JWT token");

                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_JWT_TOKEN_IS_UNSUPPORTED));

        } catch (IllegalArgumentException ex) {
                _log.error("JWT claims string is empty.");

                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_INVALID_TOKEN));
        } catch (Exception e) {
                _log.error("Invalid JWT signature.", e);

                throw new UnauthorizedException(Labels.getLabels(LabelKey.ERROR_INVALID_TOKEN));
        }

        return true;
    }

    public JwtAccessToken createAccessToken(String username) {
        try {
            int durationAccessToken = this.timeToLives.get(CacheConstants.KEYS.TOKEN);
            int durationRefreshToken = this.timeToLives.get(CacheConstants.KEYS.REFRESH_TOKEN);

            if (Validator.isNull(durationAccessToken)) {
                durationAccessToken = 5000;
            }

            if (Validator.isNull(durationRefreshToken)) {
                durationRefreshToken = 50000;
            }

            JwtToken accessToken = createAccessToken(username, durationAccessToken);

            JwtToken refreshToken = createRefreshToken(username, durationRefreshToken);

            JwtAccessToken jwtAccessToken =
                    JwtAccessToken.builder().accessToken(accessToken).refreshToken(refreshToken).build();

            // invalidate token
            this.tokenService.invalidateToken(username);

            return jwtAccessToken;
        } catch (UsernameNotFoundException e) {
            _log.error(Labels.getLabels(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD));

            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD),
                    SecurityConstants.Header.TOKEN, LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD);
        }
    }

    private JwtToken createRefreshToken(String username, int duration) {
        Map<String, Object> params = new HashMap<>();

        params.put(SecurityConstants.CLAIM.TOKEN_TYPE, SecurityConstants.TOKEN_TYPE.ACCESS_TOKEN);
        params.put(SecurityConstants.CLAIM.USER, SecurityConstants.TOKEN_TYPE.ACCESS_TOKEN);

        return createToken(username, duration, params);
    }

    private JwtToken createAccessToken(String username, int duration) {
        Map<String, Object> params = new HashMap<>();

        params.put(SecurityConstants.CLAIM.TOKEN_TYPE, SecurityConstants.TOKEN_TYPE.REFRESH_TOKEN);

        return createToken(username, duration, params);
    }

    private JwtToken createToken(String username, int duration, Map<String, Object> params) {
        Date expiration = DateUtil.getDateAfterSecond(new Date(), duration);

        String jwt = Jwts.builder()
                .subject(username)
                .claims(params)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(expiration).compact();

        return new JwtToken(jwt, duration);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        String username = claims.getSubject();

        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        if (Validator.isNull(principal)) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }
}
