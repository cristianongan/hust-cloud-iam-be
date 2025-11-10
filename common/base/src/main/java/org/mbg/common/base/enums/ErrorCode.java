package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mbg.common.label.LabelKey;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MSG0000(""),

    // Username không được để trống
    MSG1001  (LabelKey.ERROR_INVALID_USERNAME),

    // Mật khẩu không được để trống
    MSG1002  (LabelKey.ERROR_YOUR_NEW_PASSWORD_IS_MISSING),

    // Tài khoản hoặc mật khẩu không đúng
    MSG1004  (LabelKey.ERROR_INVALID_USERNAME_OR_PASSWORD),

    // Tài khoản của bạn đang bị khóa
    MSG1005  (LabelKey.ERROR_YOUR_ACCOUNT_HAS_BEEN_LOCKED),

    // Mã OTP không đúng hoặc đã hết hạn
    MSG1006  (LabelKey.ERROR_OTP_IS_INCORRECT_OR_HAS_EXPIRED),

    // Số điện thoại không được bỏ trống
    MSG1007  (LabelKey.ERROR_PHONE_NUMBER_IS_REQUIRED),

    // Số điện thoại không hợp lệ
    MSG1008  (LabelKey.ERROR_PHONE_NUMBER_IS_INVALID),

    // Mã OTP không được bỏ trống
    MSG1009  (LabelKey.ERROR_OTP_IS_MISSING),

    // Có lỗi xảy ra trong quá trình thao tác
    MSG1010  (LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED),

    // không thể gửi SMS
    MSG1011 (LabelKey.ERROR_CANNOT_SEND_SMS),

    // Mật khẩu xác thực không khớp
    MSG1012(LabelKey.ERROR_THE_PASSWORD_CONFIRMATION_DOES_NOT_MATCH),

    // Refresh token không hợp lệ
    MSG1013(LabelKey.ERROR_INVALID_REFRESH_TOKEN),

    // Mật khẩu đã được sử dụng trước đó
    MSG1014(LabelKey.ERROR_NEW_PASSWORD_CAN_NOT_BE_THE_SAME_AS_OLD_PASSWORD),

    // Tài khoản chưa đăng nhập
    MSG1015(LabelKey.ERROR_USER_IS_NOT_LOGGED_IN),

    // Dữ liệu không hợp lệ
    MSG1016(LabelKey.ERROR_INVALID_DATA),

    MSG1017(LabelKey.ERROR_SERVICE_UNAVAILABLE),

    MSG1018(LabelKey.ERROR_SERVICE_UNAVAILABLE),

    MSG1019(LabelKey.ERROR_INVALID_TOKEN),

    MSG1020(LabelKey.ERROR_OTP_CAN_NOT_BE_SENT),

    // Đạt giới hạn số lần gửi OTP
    MSG1021(LabelKey.ERROR_YOU_HAVE_REACHED_THE_LIMIT_FOR_OTP_REQUESTS),

    MSG1022(LabelKey.ERROR_TRANSACTION_ID_IS_INVALID),

    MSG1023(LabelKey.ERROR_TRANSACTION_HAS_BEEN_PROCESSED),

    MSG1024(LabelKey.ERROR_YOU_HAVE_REACHED_LIMIT_FOR_CONFIRM_OTP),

    MSG1025(LabelKey.ERROR_OTP_IS_INVALID_ENTRIES_LEFT),

    // Mã Otp hết hiệu lực. Vui lòng nhấn 'Gửi lại' OTP để nhận mã mới
    MSG1026(LabelKey.ERROR_OTP_IS_EXPIRED),

    MSG1027(LabelKey.ERROR_SUBSCRIBER_IS_INVALID),

    MSG1028(LabelKey.ERROR_CLIENT_NOT_FOUND),

    MSG1029(LabelKey.ERROR_CUSTOMER_NOT_FOUND),

    MSG1030(LabelKey.INVALID_DATA_TYPE),

    MSG1031(LabelKey.INVALID_DATA_STATUS),

    MSG1032(LabelKey.ERROR_UNAUTHORIZED),

    MSG1033(LabelKey.ERROR_DATA_DOES_NOT_EXIST),

    MSG1034(LabelKey.ERROR_DATA_ALREADY_VERIFIED),

    MSG1035(LabelKey.ERROR_EMAIL_IS_REQUIRED),

    MSG1036(LabelKey.ERROR_TEMPLATE_NOT_FOUND),

    MSG1037(LabelKey.ERROR_DUPLICATE_PRIMARY_DATA),

    MSG1038(LabelKey.ERROR_CUSTOMER_ALREADY_ACTIVE),

    MSG1039(LabelKey.ERROR_PRIMARY_DATA_CAN_NOT_BE_EMPTY),

    MSG1040(LabelKey.ERROR_EXTEND_DATA_HAS_REACH_LIMIT),

    MSG1041(LabelKey.ERROR_DUPLICATE_DATA),

    MSG1042(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND),

    MSG1043(LabelKey.ERROR_PRIMARY_DATA_CAN_NOT_DELETE),

    MSG1044(LabelKey.ERROR_DUPLICATE_PHONE),

    MSG1045(LabelKey.ERROR_DUPLICATE_EMAIL),

    MSG1046(LabelKey.ERROR_DATA_LENGTH_EXCEEDED_LIMIT),

    MSG1047(LabelKey.ERROR_INCORRECT_SIGNATURE),

    MSG1048(LabelKey.ERROR_START_OR_END_TIME_CAN_NOT_BE_EMPTY),

    MSG1049(LabelKey.ERROR_START_CAN_NOT_BE_GREAT_THAN_END_TIME),
    ;

    private String key;
}
