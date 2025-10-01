/**
 * 
 */
package org.mbg.enums;

/**
 * @author LinhLH
 *
 */
public enum OtpType {
	// Reset mật khẩu
	RESET_PASSWORD,
	
	// đổi mật khẩu
    CHANGE_PASSWORD,

	// Register
	REGISTER,
	
	// Device verify
	DEVICE_VERIFY,
	
	// Change phone number
	CHANGE_PHONE_NUMBER,

	// Nạp tiền điện thoại
	TOPUP,
	
	// Xác thực khách hàng
	CUSTOMER_VERIFY,
	
	// Khóa tài khoản
	LOCK_ACCOUNT,
	
	// Chuyển tiền
	TRANSFER
}
