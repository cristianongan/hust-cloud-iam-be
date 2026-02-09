package com.hust.iam.service;

import com.hust.common.base.model.JwtAccessToken;
import com.hust.common.base.model.JwtToken;
import com.hust.common.security.util.SecurityConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    @Cacheable(cacheNames = SecurityConstants.CACHE.REFRESH_TOKEN, key = "#username", unless = "#result == null")
    default JwtToken getRefreshToken(String username) {
        return null;
    }

    /**
     * @param username
     * @return
     */
    @Cacheable(cacheNames = SecurityConstants.CACHE.REMEMBER_ME_TOKEN, key = "#username", unless = "#result == null")
    default JwtAccessToken getRememberMeToken(String username) {
        return null;
    }

    /**
     * @param username
     * @return
     */
    @Cacheable(cacheNames = SecurityConstants.CACHE.TOKEN, key = "#username", unless = "#result == null")
    default JwtToken getToken(String username) {
        return null;
    }

    /**
     * @param username
     */
    @Caching(evict = {@CacheEvict(value = SecurityConstants.CACHE.REMEMBER_ME_TOKEN, key = "#username"),
            @CacheEvict(value = SecurityConstants.CACHE.TOKEN, key = "#username"),
            @CacheEvict(value = SecurityConstants.CACHE.REFRESH_TOKEN, key = "#username"),
            @CacheEvict(value = SecurityConstants.CACHE.FAST_LOGIN_REFRESH_TOKEN, key = "#username")})
    default String invalidateToken(String username) {
        return username;
    }

    /**
     * @param username
     */
    @Caching(evict = {@CacheEvict(value = SecurityConstants.CACHE.REMEMBER_ME_TOKEN, key = "#username"),
            @CacheEvict(value = SecurityConstants.CACHE.TOKEN, key = "#username")})
    default String invalidateCsrfToken(String username) {
        return username;
    }

    /**
     * @param username
     * @param token
     */
    @CachePut(cacheNames = SecurityConstants.CACHE.REFRESH_TOKEN, key = "#username", unless = "#result == null")
    default JwtToken saveRefreshToken(String username, JwtToken token) {
        return token;
    }

    /**
     * @param username
     * @param token
     */

    @Caching(
            put = {@CachePut(cacheNames = SecurityConstants.CACHE.REMEMBER_ME_TOKEN, key = "#username",
                    unless = "#result == null")},
            evict = {@CacheEvict(value = SecurityConstants.CACHE.TOKEN, key = "#username")})
    default JwtAccessToken saveRememberMeToken(String username, JwtAccessToken token) {
        return token;
    }

    /**
     * @param username
     * @param token
     */
    @Caching(
            put = {@CachePut(cacheNames = SecurityConstants.CACHE.TOKEN, key = "#username",
                    unless = "#result == null")},
            evict = {@CacheEvict(value = SecurityConstants.CACHE.REMEMBER_ME_TOKEN, key = "#username")})
    default JwtToken saveToken(String username, JwtToken token) {
        return token;
    }

    /**
     *
     * @param username
     * @param jwtToken
     * @return
     */
    @Caching(put = {
            @CachePut(cacheNames = SecurityConstants.CACHE.FAST_LOGIN_REFRESH_TOKEN, key = "#username",
                    unless = "#result == null")
    }, evict = {
            @CacheEvict(cacheNames = SecurityConstants.CACHE.REFRESH_TOKEN, key = "#username")
    })
    default JwtToken saveFastLoginRefreshToken(String username, JwtToken jwtToken) {
        return jwtToken;
    }

    /**
     *
     * @param username
     * @return
     */
    @Cacheable(cacheNames = SecurityConstants.CACHE.FAST_LOGIN_REFRESH_TOKEN, key = "#username")
    default JwtToken getFastLoginRefreshToken(String username) {
        return null;
    }
}
