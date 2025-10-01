/**
 * 
 */
package org.mbg.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * @author LinhLH
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {
	private boolean enable;

	private String defaultOtp;

	private int numberOfDigits;

	private String template;
	
	private int otpAttempt;
	
	private int duration;
	
	private int otpConfirmAttemp;
	
	private int otpResendAttemp;
}
