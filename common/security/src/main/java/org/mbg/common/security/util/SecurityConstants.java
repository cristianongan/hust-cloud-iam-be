package org.mbg.common.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for Spring Security authorities.
 * This utility class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

	/**
	 * System roles.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class SystemRole {
		public static final String USER = "ROLE_USER";
		public static final String ANONYMOUS = "ROLE_ANONYMOUS";
		public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	}

	/**
	 * Action privileges.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Privilege {
		public static final String ADMINISTRATOR = "ADMINISTRATOR";
		public static final String APPROVAL = "APPROVAL";
		public static final String APPROVAL_REQUEST = "APPROVAL_REQUEST";
		public static final String CREATE = "CREATE";
		public static final String DELETE = "DELETE";
		public static final String EXPORT = "EXPORT";
		public static final String IMPORT = "IMPORT";
		public static final String READ = "READ";
		public static final String WRITE = "WRITE";
	}

	/**
	 * Specific account identifiers.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Account {
		public static final String SUPER_ADMIN = "superadmin";
	}

	/**
	 * Common HTTP Header names.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Header {
		public static final String AUTHORIZATION_HEADER = "Authorization";
		public static final String BEARER_START = "Bearer ";
		public static final String BASIC_START = "Basic ";
		public static final String PRIVILEGES = "privileges";
		public static final String HASH_KEY = "hash-key";
		public static final String LOCALE = "locale";
		public static final String TOKEN = "token";
		public static final String USER = "user";
		public static final String X_REQUEST_ID = "X-Request-Id";
		public static final String REQUEST_ID_MDC = "requestId";
		public static final String X_USER_ID = "X-User-Id";
		public static final String X_SERVICE_PERMISSIONS = "X-Service-Permissions";
		public static final String X_SERVICE_PERMISSIONS_SIGNATURE = "X-Service-Permissions-Signature";
		public static final String X_ENC_KEY = "X-Enc-Key";
		public static final String X_Nonce = "X-Nonce";
		public static final String X_SIG = "X-Signature";
		public static final String ORG = "Org";
	}

	/**
	 * Common Cookie names.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Cookie {
		public static final String JWT_TOKEN = "mbg-jwtToken";
	}

	/**
	 * Cryptographic algorithm names.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Algorithm {
		public static final String RSA = "RSA";
	}

	/**
	 * General URL patterns.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class UrlPattern {
		public static final String ALL = "/**";
		public static final String API = "/api/**";
	}

	/**
	 * Prefixes used for configuration properties.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class PropertyPrefix {
		public static final String SECURITY = "security";
		public static final String RSA = "rsa";
	}

	/**
	 * Defines public URL patterns.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class PublicUrlPattern {
		public static final String PUBLIC = "/public/**";

		/**
		 * Returns the defined public URL patterns as a String array.
		 *
		 * @return An array containing public URL patterns.
		 */
		public static String[] getPublicUrlPattern() {
			return new String[]{
					PUBLIC
			};
		}
	}

	public static final class TOKEN_TYPE {
		public static final String ACCESS_TOKEN = "access_token";

		public static final String REFRESH_TOKEN = "refresh_token";
	}

	public static final class CLAIM {
		public static final String TOKEN_TYPE = "token_type";

		public static final String USER = "user";
	}

	public static final class CACHE {
		public static final String REFRESH_TOKEN =  "refresh_token";

		public static final String ACCESS_TOKEN = "access_token";

		public static final String REMEMBER_ME_TOKEN = "remember_me";

		public static final String TOKEN = "token";

		public static final String FAST_LOGIN_REFRESH_TOKEN =  "fast_login_refresh_token";

		public static final String TRANSACTION = "transaction";
	}
}