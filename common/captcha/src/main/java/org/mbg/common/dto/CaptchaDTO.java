package org.mbg.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for handling CAPTCHA-related data.
 * <p>
 * This class encapsulates information including a transaction ID and CAPTCHA value.
 * <p>
 * It provides getter and setter methods for the properties and includes an all-args constructor.
 * <p>
 * This class is typically used in contexts where CAPTCHA validation or verification is required.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Getter
@Setter
@AllArgsConstructor
public class CaptchaDTO {
	private String transactionId;
	
	private String captcha;
}
