package com.hust.common.security.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * class for Spring Security.
 */
public class SecurityUtils {

	public static List<GrantedAuthority> getAuthorities(List<String> authorities) {
		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	/**
	 * Get the JWT of the current user.
	 *
	 * @return the JWT of the current user.
	 */
	public static Optional<String> getCurrentUserJWT() {
		SecurityContext securityContext = SecurityContextHolder.getContext();

		return Optional.ofNullable(securityContext.getAuthentication())
				.filter(authentication -> authentication.getCredentials() instanceof String)
				.map(authentication -> (String) authentication.getCredentials());
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user.
	 */
	public static Optional<String> getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();

		return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
			if (authentication instanceof AnonymousAuthenticationToken) {
				return null;
			}

			if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {

                return springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				return (String) authentication.getPrincipal();
			}

			return null;
		});
	}

	public static List<String> getNameAuthorities(Collection<GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}

	/**
	 * Check if a user is authenticated.
	 *
	 * @return true if the user is authenticated, false otherwise.
	 */
	public static boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();

		return Optional.ofNullable(securityContext.getAuthentication())
				.map(authentication -> !authentication.getAuthorities().isEmpty()).orElse(false);
	}

	public static String[] getBasicAuthentication(String token) {
		if (token == null) {
			return null;
		}

		String rsToken = null;

		if (StringUtils.hasText(token) && token.startsWith(SecurityConstants.Header.BASIC_START)) {
			rsToken = token.substring(SecurityConstants.Header.BASIC_START.length());
		}

        assert rsToken != null;

        return new String(Base64.getDecoder().decode(rsToken.getBytes())).split(":");
	}
}
