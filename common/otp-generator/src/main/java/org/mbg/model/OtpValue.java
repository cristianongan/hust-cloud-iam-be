package com.hust.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpValue implements Serializable {

	private static final long serialVersionUID = 5000814930976922317L;

	private String otp;
	
	private String transactionId;

	private int count;
	
	private int otpConfirmCount;
	
	public OtpValue(String otp, String transactionId) {
		this.otp = otp;
		this.transactionId = transactionId;
		this.count = 1;
		this.otpConfirmCount = 0;
	}

	public OtpValue(String otp, String transactionId, int count) {
		this.otp = otp;
		this.transactionId = transactionId;
		this.count = count + 1;
		this.otpConfirmCount = 0;
	}
	
	public OtpValue(String otp, String transactionId, int count, int otpConfirmCount) {
	    this.otp = otp;
        this.transactionId = transactionId;
        this.count = count;
        this.otpConfirmCount = otpConfirmCount + 1;
	}
}
