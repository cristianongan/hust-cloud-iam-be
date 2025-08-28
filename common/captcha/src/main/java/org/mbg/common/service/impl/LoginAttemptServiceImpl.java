package org.mbg.common.service.impl;

import org.mbg.common.repository.LoginFailedRepository;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link LoginAttemptService} interface for managing login attempts, failures,
 * and determining whether CAPTCHA is required.
 *
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Record successful login attempts and invalidate related cache data.</li>
 *   <li>Record failed login attempts and log warning messages for repeatedly failing users.</li>
 *   <li>Determine if a CAPTCHA is required based on the number of failed login attempts.</li>
 * </ul>
 *
 * <p>
 * It utilizes a repository for tracking failed login attempts and integrates authentication
 * properties to define constraints, such as the maximum allowed failed attempts before requiring CAPTCHA.
 *
 * <p>
 * This service is aimed at enhancing security by mitigating brute-force login attempts.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {
	private final AuthenticationProperties authenticationProperties;

	private final LoginFailedRepository loginFailedRepository;

	/**
	 * Records a successful login attempt and invalidates the cache entry associated with the provided key.
	 * <p>
	 * This method is intended to clear data related to failed login attempts after
	 * a successful authentication, enhancing system security and maintaining accurate tracking.
	 *
	 * @param key the unique identifier used to invalidate the associated failed login attempt data.
	 */
	@Override
	public void loginSucceeded(String key) {
		this.loginFailedRepository.invalidate(key);
	}

	/**
	 * Retrieves the number of failed login attempts associated with the provided key.
	 * <p>
	 * If there are no stored attempts in the cache for the given key, the method will return 0.
	 *
	 * @param key the unique identifier used to fetch the associated failed login attempt count.
	 * @return the number of failed login attempts associated with the specified key, or 0 if no data is present.
	 */
	private int getAttempts(String key) {
		return Optional.ofNullable(this.loginFailedRepository.getIfPresent(key))
				.orElse(0);
	}

	/**
	 * Records a failed login attempt and updates the failure count for the given key.
	 * <p>
	 * This method increments the number of failed login attempts for a user identified by the key.
	 * Additionally, it logs a warning message when a failure occurs.
	 *
	 * @param key the unique identifier for tracking the failed login attempts.
	 */
	@Override
	public void loginFailed(String key) {
		int attempts = this.getAttempts(key);

		attempts++;

		_log.warn("[loginFailed] User with ip {} login failure for {} times", key, attempts);

		this.loginFailedRepository.put(key, attempts);
	}

	/**
	 * Determines whether a CAPTCHA is required based on the number of failed login attempts.
	 * <p>
	 * This method evaluates the failed login attempts associated with the provided key
	 * and compares it to the maximum allowed login attempt threshold defined
	 * in the authentication properties.
	 *
	 * @param key the unique identifier used to track the failed login attempts for a user.
	 * @return {@code true} if the number of failed attempts reaches or exceeds the maximum
	 *         login attempt threshold, {@code false} otherwise.
	 */
	@Override
	public boolean isRequiredCaptcha(final String key) {
		return this.getAttempts(key) >= this.authenticationProperties.getAuthentication().getUser().getLoginMaxAttemptTime();
	}
}
