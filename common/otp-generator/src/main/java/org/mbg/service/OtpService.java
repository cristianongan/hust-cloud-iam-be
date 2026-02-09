package com.hust.service;

import com.hust.enums.OtpType;
import com.hust.model.OtpValue;

public interface OtpService {
	OtpValue findByKey(String phoneNumber, OtpType type);
	
	void invalidateOtp(String phoneNumber, OtpType type);
	
	String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate);
	
	String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate, boolean otpDefault);

	String sendOtpViaEmail(String email, OtpType type, boolean validate);

	String sendOtpViaEmail(String email, OtpType type, boolean validate, boolean otpDefault);
	
	void validateOtp(String phoneNumber, String transactionId, OtpType type, String otp);
}
