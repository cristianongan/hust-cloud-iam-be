package com.hust.common.api.util;

import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import com.hust.common.util.Validator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public final class HeaderUtil {
	/**
	 * <p>
	 * createAlert.
	 * </p>
	 *
	 * @param message a {@link String} object.
	 * @param param a {@link String} object.
	 * @return a {@link HttpHeaders} object.
	 */
	public static HttpHeaders createAlert(String message, String param) {
		HttpHeaders headers = new HttpHeaders();

		headers.add(ApiConstants.HttpHeaders.X_ACTION_MESSAGE, message);

        headers.add(ApiConstants.HttpHeaders.X_ACTION_PARAMS,
                URLEncoder.encode(param, StandardCharsets.UTF_8));

        return headers;
	}

	/**
	 * <p>
	 * createEntityCreationAlert.
	 * </p>
	 *
	 * @param applicationName a {@link String} object.
	 * @param enableTranslation a boolean.
	 * @param entityName a {@link String} object.
	 * @param param a {@link String} object.
	 * @return a {@link HttpHeaders} object.
	 */
	public static HttpHeaders createEntityCreationAlert(String applicationName, boolean enableTranslation,
			String entityName, String param) {
		String message = enableTranslation ? applicationName + "." + entityName + ".created"
				: "A new " + entityName + " is created with identifier " + param;

		return createAlert(message, param);
	}

	/**
	 * <p>
	 * createEntityDeletionAlert.
	 * </p>
	 *
	 * @param applicationName a {@link String} object.
	 * @param enableTranslation a boolean.
	 * @param entityName a {@link String} object.
	 * @param param a {@link String} object.
	 * @return a {@link HttpHeaders} object.
	 */
	public static HttpHeaders createEntityDeletionAlert(String applicationName, boolean enableTranslation,
			String entityName, String param) {
		String message = enableTranslation ? applicationName + "." + entityName + ".deleted"
				: "A " + entityName + " is deleted with identifier " + param;

		return createAlert(message, param);
	}

	/**
	 * <p>
	 * createEntityUpdateAlert.
	 * </p>
	 *
	 * @param applicationName a {@link String} object.
	 * @param enableTranslation a boolean.
	 * @param entityName a {@link String} object.
	 * @param param a {@link String} object.
	 * @return a {@link HttpHeaders} object.
	 */
	public static HttpHeaders createEntityUpdateAlert(String applicationName, boolean enableTranslation,
			String entityName, String param) {
		String message = enableTranslation ? applicationName + "." + entityName + ".updated"
				: "A " + entityName + " is updated with identifier " + param;

		return createAlert(message, param);
	}

	/**
	 * <p>
	 * createFailureAlert.
	 * </p>
	 *
	 * @param enableTranslation a boolean.
	 * @param entityName a {@link String} object.
	 * @param errorKey a {@link String} object.
	 * @param defaultMessage a {@link String} object.
	 * @return a {@link HttpHeaders} object.
	 */
	public static HttpHeaders createFailureAlert(boolean enableTranslation, String entityName, String errorKey,
			String defaultMessage) {
		_log.error("Entity processing failed, {}", defaultMessage);

		String message = enableTranslation ? errorKey : defaultMessage;

		HttpHeaders headers = new HttpHeaders();

		headers.add(ApiConstants.HttpHeaders.X_ACTION_MESSAGE, message);
		headers.add(ApiConstants.HttpHeaders.X_ACTION_PARAMS, entityName);

		return headers;
	}

	public static String getBasicAuthorization(String username, String password) {
        String sb = username +
                StringPool.COLON +
                password;

		byte[] encodedAuth = Base64.encodeBase64(sb.getBytes(StandardCharsets.UTF_8));

		return SecurityConstants.Header.BASIC_START + new String(encodedAuth);
	}

	public static String[] decodeBasicAuthorization(String basicAuthorization) {
		if (Validator.isNull(basicAuthorization)) {
			return null;
		}

		byte[] decodedAuth = Base64.decodeBase64(basicAuthorization);
		String auth = new String(decodedAuth, StandardCharsets.UTF_8);

        return auth.split(StringPool.COLON);
	}

	public static String getBearerAuthorization(String token) {
		return SecurityConstants.Header.BEARER_START + token;
	}

	public static String getAuthorization(String tokenType, String token) {
		StringBuilder sb = new StringBuilder(3);

		if (Objects.nonNull(tokenType)) {
			sb.append(tokenType);
			sb.append(StringPool.SPACE);
		} else {
			sb.append(SecurityConstants.Header.BEARER_START);
		}

		sb.append(token);

		return sb.toString();
	}
	
	public static HttpHeaders getTypeJsonHeaders() {
		HttpHeaders headers = getHeaders();

		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	public static HttpHeaders getTypeUrlEncodeHeaders() {
		HttpHeaders headers = getHeaders();

		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		return headers;
	}

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();

		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.3179.54");

		return headers;
	}

	private HeaderUtil() {}
}
