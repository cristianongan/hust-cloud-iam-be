package com.hust.common.service;

import com.hust.common.dto.CaptchaDTO;

import java.util.Map;

/**
 * Service interface for handling CAPTCHA operations.
 * <p>
 * This interface provides methods to generate CAPTCHA, retrieve necessary CAPTCHA-related details,
 * and validate CAPTCHA input from users to ensure correctness.
 *
 * <p>
 * It can be implemented to provide various CAPTCHA-related functionalities,
 * such as validating user sessions or preventing automated attacks.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface CaptchaService {
	CaptchaDTO generate();
	
	Map<String, Object> generateRequired();
	
	boolean isInvalidCaptcha(String transactionId, String inputCaptcha);
}
