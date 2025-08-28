package org.mbg.common.service;

/**
 * Service interface for managing login attempts and determining CAPTCHA requirements.
 * <p>
 * This interface provides methods to log successful and failed login attempts
 * and to check if showing a CAPTCHA is necessary based on failed attempts.
 * <p>
 * It is designed to help mitigate brute-force login attacks by implementing
 * a captcha mechanism after several failed login attempts.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface LoginAttemptService {
	void loginSucceeded(final String key);
	
	void loginFailed(final String key);
	
	boolean isRequiredCaptcha(final String key);
}
