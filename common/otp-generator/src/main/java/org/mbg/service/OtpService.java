package org.mbg.service;

import org.mbg.enums.OtpType;
import org.mbg.model.OtpValue;

public interface OtpService {
	OtpValue findByKey(String phoneNumber, OtpType type);
	
	void invalidateOtp(String phoneNumber, OtpType type);
	
	String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate);
	
	String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate, boolean otpDefault);
	
	void validateOtp(String phoneNumber, String transactionId, OtpType type, String otp);
}
