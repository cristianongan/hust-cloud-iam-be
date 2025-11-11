package org.mbg.common.base.enums;

public enum TemplateCode {
    SMS_OTP,

    EMAIL_OTP,

    DATA_DESCRIPTION,

    NEW_DEVICE_LOGIN_NOTICE,

    SMS_RESET_PASSWORD,

    SMS_NEW_CUSTOMER,

    EMAIL_NEW_CUSTOMER,

    SMS_APPROVAL_CREATE_CUSTOMER,

    SMS_APPROVAL_UNLOCK,

    SMS_APPROVAL_LOCK,

    SMS_APPROVAL_DELETE,

    PASSWORD_EXPIRED_NOTICE,

    SMS_ACCOUNT_IS_TEMP_LOCKED,

    SMS_ACCOUNT_IS_CLOSED,

    SMS_REISSUE_PASSWORD,

    SEND_MAIL_ACTIVE, // send mail link verify in active user

    SEND_MAIL_INFORMATION, // send mail link information in active user

    SEND_MAIL_FORGOT_PASSWORD, // Send mail link set up password on forgot password

    BALANCE_CHANGE_TRANSFER_MONEY, // Biến động số dư tài khoản chuyển tiền

    BALANCE_CHANGE_RECEIVE_MONEY, // Biến động số dư tài khoản nhận tiền

    BALANCE_CHANGE_PAY_MERCHANT // Biến động số dư các thanh toán liên quan đến merchant
}
