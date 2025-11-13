package org.mbg.common.label;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class defining constants for label keys used in internationalization (i18n).
 * These keys typically correspond to entries in resource bundles (e.g., .properties files).
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * <p>
 * 07/04/2025 - LinhLH: Create new
 * </p>
 *
 * @author LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LabelKey {

	public static final String ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED = "error.an-unexpected-error-has-occurred";

	public static final String ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED_WHEN_EXPORT =
			"error.an-unexpected-error-has-occurred-when-export";

	public static final String ERROR_APPROVE_WAS_NOT_SUCCESSFUL = "error.approve-was-not-successful";

	public static final String ERROR_BAD_REQUEST = "error.bad-request";

	public static final String ERROR_CANNOT_ASSIGN_ROLE_THAT_HAS_LOWER_LEVEL_THAN_YOURS =
			"error.cannot-assign-role-that-has-lower-level-than-yours";

	public static final String ERROR_CANNOT_CREATE_A_ROLE_THAT_HAS_LOWER_LEVEL_THAN_YOURS =
			"error.you-cannot-create-a-role-has-lower-level-roles-than-yours";

	public static final String ERROR_CANNOT_CREATE_DATA_WITH_EXISTED_ID = "error.cannot-create-data-with-existed-id";

	public static final String ERROR_CANNOT_EXPORT_TOO_MUCH_RECORD = "error.cannot-export-too-much-record";

	public static final String ERROR_CANNOT_OVERRIDE_DATA = "error.cannot-override-data";

	public static final String ERROR_CANNOT_UPDATE_THE_ROLE_THAT_HAS_LOWER_LEVEL_THAN_YOURS =
			"error.you-cannot-update-the-role-has-lower-level-roles-than-yours";

	public static final String ERROR_CONCURRENCY_FAILURE = "error.concurrency-failure";

	public static final String ERROR_CONSTRAINT_VIOLATION = "error.constraint-violation";

	public static final String ERROR_DATA_COULD_NOT_BE_FOUND = "error.data-could-not-be-found";

	public static final String ERROR_DATA_DOES_NOT_EXIST = "error.data-does-not-exist";

	public static final String ERROR_DATA_DOES_NOT_EXIST_OR_YOU_ARE_NOT_ALLOWED_TO_PERFORM_THIS_ACTION =
			"error.data-does-not-exist-or-you-are-not-allowed-to-perform-this-action";

	public static final String ERROR_DATA_DOES_NOT_EXIST_WITH_CODE = "error.data-does-not-exist-with-code";

	public static final String ERROR_DATA_DOES_NOT_EXIST_WITH_NAME = "error.data-does-not-exist-with-name";

	public static final String ERROR_DATA_IS_INCORRECT_WITH_NAME_OR_CODE = "error.data-is-incorrect-with-name-or-code";

	public static final String ERROR_DATE_CANNOT_BE_IN_THE_FUTURE = "error.date-cannot-be-in-the-future";

	public static final String ERROR_DATE_CANNOT_BE_IN_THE_PAST = "error.date-cannot-be-in-the-past";

	public static final String ERROR_DATE_INJECT_REGISTRATION_CANNOT_LESS_THAN_NOW =
			"error.date-inject-registration-cannot-less-than-now";

	public static final String ERROR_DATE_OF_BIRTH_CANNOT_GREATER_THAN_NOW =
			"error.date-of-birth-cannot-greater-than-now";

	public static final String ERROR_DELETED_WAS_NOT_SUCCESSFUL = "error.deleted-was-not-successful";

	public static final String ERROR_DUPLICATE_DATA = "error.duplicate-data";

	public static final String ERROR_EXCEED_MAX_LENGTH = "error.exceed-max-length";

	public static final String ERROR_INCORRECT_CAPTCHA = "error.incorrect-captcha";

	public static final String ERROR_INCORRECT_OTP = "error.incorrect-otp";

	public static final String ERROR_INCORRECT_SIGNATURE = "error.incorrect-signature";

	public static final String ERROR_INPUT_CANNOT_BE_EMPTY = "error.input-cannot-be-empty";

	public static final String ERROR_INPUT_INVALID_LENGTH = "error.input-invalid-length";

	public static final String ERROR_INPUT_INVALID_MAX_LENGTH = "error.input-invalid-max-length";

	public static final String ERROR_INPUT_MUST_BE_ASSIGNED = "error.input-must-be-assigned";

	public static final String ERROR_INPUT_MUST_BE_NON_NEGATIVE = "error.input-must-be-non-negative";

	public static final String ERROR_INPUT_MUST_BE_POSITIVE = "error.input-must-be-positive";

	public static final String ERROR_INVALID = "error.invalid";

	public static final String ERROR_INVALID_AREA = "error.invalid-area";

	public static final String ERROR_INVALID_DATA = "error.invalid-data";

	public static final String ERROR_INVALID_DATA_FORMAT = "error.invalid-data-format";

	public static final String ERROR_INVALID_EXCEL_TEMPLATE = "error.invalid-excel-template";

	public static final String ERROR_INVALID_INPUT_DATA = "error.invalid-input-data";

	public static final String ERROR_INVALID_REFRESH_TOKEN = "error.invalid-refresh-token";

	public static final String ERROR_INVALID_TOKEN = "error.invalid-token";

	public static final String ERROR_INVALID_USERNAME = "error.invalid-username";

	public static final String ERROR_INVALID_USERNAME_OR_PASSWORD = "error.invalid-username-or-password";

	public static final String ERROR_LIMIT_OTP_ATTEMPTS = "error.limit-otp-attempts";

	public static final String ERROR_LOCATION_OF_CHILDREN_MUST_BE_THE_SAME_AS_THE_PARENT =
			"error.location-of-children-must-be-the-same-as-the-parent";

	public static final String ERROR_MAXIMUM_NUMBER_OF_OTP_REACHED = "error.maximum-number-of-otp-reached";

	public static final String ERROR_METHOD_ARGUMENT_NOT_VALID = "error.method-argument-not-valid";

	public static final String ERROR_MORE_THAN_ONE_DATA_WITH_THE_GIVEN_NAME_WAS_FOUND =
			"error.more-than-one-data-with-the-given-name-was-found";

	public static final String ERROR_NEW_PASSWORD_CAN_NOT_BE_THE_SAME_AS_OLD_PASSWORD =
			"error.new-password-can-not-be-the-same-as-old-password";

	public static final String ERROR_NO_DATA_TO_IMPORT = "error.no-data-to-import";

	public static final String ERROR_OTP_HAS_EXPIRED = "error.otp-has-expired";

	public static final String ERROR_OTP_IS_INCORRECT_OR_HAS_EXPIRED = "error.otp-is-incorrect-or-has-expired";

	public static final String ERROR_OTP_IS_MISSING = "error.otp-is-missing";

	public static final String ERROR_OTP_IS_NOT_SENT = "error.otp-is-not-sent";

	public static final String ERROR_SERVICE_UNAVAILABLE = "error.service-unavailable";

	public static final String ERROR_SERVICE_UNAVAILABLE_TRY_AGAIN = "error.service-unavailable-try-again";

	public static final String ERROR_SOME_DATA_ARE_MISSING = "error.some-data-are-missing";

	public static final String ERROR_THE_FILE_DOES_NOT_EXIST = "error.the-file-does-not-exist";

	public static final String ERROR_THE_PASSWORD_CONFIRMATION_DOES_NOT_MATCH =
			"error.the-password-confirmation-does-not-match";

	public static final String ERROR_UNAUTHORIZED = "error.unauthorized";

	public static final String ERROR_USER_COULD_NOT_BE_FOUND = "error.user-could-not-be-found";

	public static final String ERROR_USER_IS_NOT_LOGGED_IN = "error.user-is-not-logged-in";

	public static final String ERROR_YOU_CANNOT_LOCK_YOURSELF = "error.you-cannot-lock-yourself";

	public static final String ERROR_YOU_CANNOT_UPDATE_THE_USER_HAS_LOWER_LEVEL_ROLES_THAN_YOURS =
			"error.you-cannot-update-the-user-has-lower-level-roles-than-yours";

	public static final String ERROR_YOU_DO_NOT_HAVE_PERMISSION_TO_ASSIGN_THIS_DISTRICT =
			"error.you-do-not-have-permission-to-assign-this-district";

	public static final String ERROR_YOU_DO_NOT_HAVE_PERMISSION_TO_ASSIGN_THIS_PROVINCE =
			"error.you-do-not-have-permission-to-assign-this-province";

	public static final String ERROR_YOU_DO_NOT_HAVE_PERMISSION_TO_ASSIGN_THIS_WARD =
			"error.you-do-not-have-permission-to-assign-this-ward";

	public static final String ERROR_YOU_MIGHT_NOT_HAVE_PERMISSION_TO_ASSIGN_THIS_DATA =
			"error.you-might-not-have-permission-to-assign-this-data";

	public static final String ERROR_YOU_MIGHT_NOT_HAVE_PERMISSION_TO_PERFORM_THIS_ACTION =
			"error.you-might-not-have-permission-to-perform-this-action";

	public static final String ERROR_YOU_MUST_ENTER_A_VALUE_FOR_ALL_REQUIRED_FIELDS =
			"error.you-must-enter-a-value-for-all-required-fields";

	public static final String ERROR_YOU_MUST_ENTER_A_VALUE_FOR_REQUIRED_FIELD =
			"error.you-must-enter-a-value-for-required-field";

	public static final String ERROR_YOU_ONLY_CAN_PERFORM_THIS_ACTION_ON_DATA_THAT_HAS_STATUS =
			"error.you-only-can-perform-this-action-on-data-that-has-status";

	public static final String ERROR_YOU_ONLY_CAN_PERFORM_THIS_ACTION_ON_THE_DATA_THAT_YOU_HAVE_CREATED =
			"error.you-only-can-perform-this-action-on-the-data-that-you-have-created";

	public static final String ERROR_YOUR_CONFIRM_PASSWORD_IS_NOT_MATCH = "error.your-confirm-password-is-not-match";

	public static final String ERROR_YOUR_CURRENT_PASSWORD_IS_INCORRECT = "error.your-current-password-is-incorrect";

	public static final String ERROR_YOUR_CURRENT_PASSWORD_IS_MISSING = "error.your-current-password-is-missing";

	public static final String ERROR_YOUR_CURRENT_PASSWORD_IS_MISSING_OR_INCORRECT =
			"error.your-current-password-is-missing-or-incorrect";

	public static final String ERROR_YOUR_NEW_PASSWORD_IS_MISSING = "error.your-new-password-is-missing";

	public static final String ERROR_YOUR_NEW_PASSWORD_IS_MISSING_OR_CONFIRM_PASSWORD_NOT_MATCH =
			"error.your-new-password-is-missing-or-confirm-password-not-match";

	public static final String ERROR_YOUR_REGISTRATION_WAS_NOT_SUCCESSFUL =
			"error.your-registration-was-not-successful";

	public static final String ERROR_YOU_ARE_NOT_ELIGIBLE_FOR_LOAN = "error.you-are-not-eligible-for-loan";

	public static final String ERROR_SESSION_IS_INVALID = "error.session-is-invalid";

	public static final String LABEL_ADDRESS = "label.address";

	public static final String LABEL_CONFIRM_PASSWORD = "label.confirm-password";

	public static final String LABEL_CONTACT_PHONE_NUMBER = "label.contact-phone-number";

	public static final String LABEL_COORDINATE = "label.coordinate";

	public static final String LABEL_DATE_OF_BIRTH = "label.date-of-birth";

	public static final String LABEL_DISTRICT = "label.district";

	public static final String LABEL_EMAIL = "label.email";

	public static final String LABEL_FILE = "label.file";

	public static final String LABEL_FULLNAME = "label.fullname";

	public static final String LABEL_GENDER = "label.gender";

	public static final String LABEL_HEADACHE = "label.headache";

	public static final String LABEL_IDENTIFICATION = "label.identification";

	public static final String LABEL_JOB = "label.job";

	public static final String LABEL_LEVEL = "label.level";

	public static final String LABEL_NATION = "label.nation";

	public static final String LABEL_NOTE = "label.note";

	public static final String LABEL_PASSWORD = "label.password";

	public static final String LABEL_PHONE_NUMBER = "label.phone-number";

	public static final String LABEL_PROVINCE = "label.province";

	public static final String LABEL_ROLE = "label.role";

	public static final String LABEL_ROLE_NAME = "label.role-name";

	public static final String LABEL_STATUS = "label.status";

	public static final String LABEL_STATUS_DRAFT = "label.status-draf"; // Note: Original had "draf", kept as is

	public static final String LABEL_STATUS_NOT_SENT = "label.status-not-sent";

	public static final String LABEL_USER = "label.user";

	public static final String LABEL_USERNAME = "label.username";

	public static final String LABEL_WARD = "label.ward";

	public static final String MESSAGE_FIRST = "message.first";

	public static final String MESSAGE_LOGIN_SUCCESSFUL = "message.login-successful";

	public static final String MESSAGE_OTP_HAS_BEEN_SENT_TO_YOUR_EMAIL_ADDRESS =
			"message.otp-has-been-sent-to-your-email-address";

	public static final String MESSAGE_OTP_HAS_BEEN_SENT_TO_YOUR_MOBILE_NUMBER =
			"message.otp-has-been-sent-to-your-mobile-number";

	public static final String MESSAGE_REQUEST_FOR_APPROVAL_WAS_SUCCESSFUL =
			"message.request-for-approval-was-successful";

	public static final String MESSAGE_SECOND = "message.second";

	public static final String MESSAGE_SERVER_HAS_BEEN_STARTED = "message.server-has-been-started";

	public static final String MESSAGE_SUCCESSFUL = "message.successful";

	public static final String MESSAGE_THIRD = "message.third";

	public static final String MESSAGE_YOU_HAVE_SUCCESSFULLY_CHANGED_PASSWORD =
			"message.you-have-successfully-changed-password";

	public static final String MESSAGE_YOU_HAVE_SUCCESSFULLY_REGISTERED_FOR_AN_ACCOUNT =
			"message.you-have-successfully-registered-for-an-account";

	public static final String MESSAGE_YOU_HAVE_SUCCESSFULLY_VERIFIED_YOUR_ACCOUNT =
			"message.you-have-successfully-verified-your-account";

	public static final String LABEL_TOKEN = "label.token";

	public static final String ERROR_YOUR_ACCOUNT_HAS_BEEN_LOCKED = "error.your-account-has-been-locked";

	public static final String LABEL_IDCODE = "label.idcode";

	public static final String LABEL_TXDATE = "label.txdate";

	public static final String LABEL_SYMBOL = "label.symbol";

	public static final String LABEL_CUSTODYCD = "label.custodycd";

	public static final String LABEL_SRTYPE = "label.srtype";

	public static final String LABEL_ORDERTYPE = "label.ordertype";

	public static final String LABEL_ORDERQTTY = "label.orderqtty";

	public static final String LABEL_ORDERID = "label.orderid";

	public static final String LABEL_OTP = "label.otp";

	public static final String LABEL_CODEID = "label.codeid";

	public static final String LABEL_BANKACCOUNTCUS = "label.bankaccountcus";

	public static final String LABEL_BANK_ACCOUNT = "label.bank-account";

	public static final String LABEL_AMT = "label.amt";

	public static final String LABEL_TRANSFER_STATUS = "label.transfer-status";

	public static final String LABEL_PAYMENT_AUTO = "label.payment-auto";

	public static final String LABEL_IDDATE = "label.iddate";

	public static final String LABEL_IDPLACE = "label.idplace";

	public static final String LABEL_CITYBANK = "label.citybank";

	public static final String LABEL_TYPE = "label.type";

	public static final String LABEL_ORDER_DATE = "label.order-date";

	public static final String LABEL_ORDER_AMOUNT = "label.order-amount";

	public static final String LABEL_SALE_ID = "label.sale-id";

	public static final String LABEL_BILL_PAYMENT = "label.bill-payment";

	public static final String LABEL_SERVICE = "label.service";

	public static final String LABEL_BILL_INFO = "label.bill-info";

	public static final String LABEL_PAYMENT_INFO = "label.payment-info";

	public static final String LABEL_CHARGE_INFO = "label.charge-info";

	public static final String LABEL_DEBIT = "label.debit";

	public static final String LABEL_CHARGE_CODE = "label.charge-code";

	public static final String LABEL_CURRENCY = "label.currency";

	public static final String LABEL_CHART_SOURCE = "label.charge-source"; // Note: Original had "chart", kept as is

	public static final String LABEL_REQUEST_ID = "label.request-id";

	public static final String LABEL_SOURCE_NAME = "label.source-name";

	public static final String LABEL_SOURCE_NUMBER = "label.source-number";

	public static final String LABEL_FINAL_TIME = "label.final-time";

	public static final String LABEL_MAX_RETRY = "label.max-retry";

	public static final String LABEL_AUTO_PAYMENT_ID = "label.auto-payment-id";

	public static final String LABEL_RECORD_ID = "label.record-id";

	public static final String LABEL_CHECK_ROUND = "label.check-round";

	public static final String LABEL_LOAN_TYPE = "label.loan-type";

	public static final String LABEL_LOAN_TERM = "label.loan-term";

	public static final String LABEL_LOAN_PURPOSE = "label.loan-purpose";

	public static final String LABEL_DRAWDOWN_ACCOUNT = "label.drawdown-account";

	public static final String LABEL_FIELD_CODE = "label.field-code";

	public static final String LABEL_FARMER_CODE = "label.farmer-code";

	public static final String LABEL_AGENT_CODE = "label.agent-code";

	public static final String LABEL_SUPPLICATION_CODE = "label.supplication-code";

	public static final String LABEL_REQUEST_CODE = "label.request-code";

	public static final String ERROR_USER_CAN_NOT_USE_DOTP = "error.user-can-not-use-dotp";

	public static final String LABEL_TRANSACTION_ID = "label.transaction-id";

	public static final String ERROR_JWT_TOKEN_IS_UNSUPPORTED =  "error.jwt-token-is-unsupported";

	public static final String ERROR_TOKEN_HAS_EXPIRED = "error.token-has-expired";

	public static final String ERROR_TOKEN_MALFORMED = "error.token-malformed";

	public static final String LABEL_DELETED = "label.deleted";

	public static final String LABEL_INACTIVE = "label.inactive";

	public static final String LABEL_NEW = "label.new";

	public static final String LABEL_ACTIVE =  "label.active";

	public static final String LABEL_PENDING =  "label.pending";

	public static final String LABEL_WAITING = "label.waiting";

	public static final String LABEL_DONE = "label.done";

	public static final String ERROR_INVALID_CREDENTIAL = "error.invalid-credential";

	public static final String ERROR_CLIENT_NOT_FOUND = "error.client-not-found";

	public static final String LABEL_FILTER = "label.filter";

	public static final String LABEL_LOCK = "label.lock";

	public static final String LABEL_QUOTA = "label.quota";

	public static final String ERROR_QUOTA_IS_SMALLER_THEN_IN_USE = "error.quota-is-smaller-than-in-use";

	public static final String LABEL_START_TIME = "label.start-time";

	public static final String LABEL_END_TIME = "label.end-time";

	public static final String ERROR_PHONE_NUMBER_IS_INVALID = "error.phone-number-is-invalid";

	public static final String ERROR_MESSAGE_IS_REQUIRED = "error.message-is-required";

	public static final String ERROR_CANNOT_SEND_SMS = "error.cannot-send-sms";

	public static final String LABEL_TEMPLATE = "label.template";

	public static final String ERROR_PHONE_NUMBER_IS_REQUIRED = "error.phone-number-is-required";

	public static final String ERROR_EMAIL_IS_REQUIRED = "error.email-is-required";

	public static final String ERROR_OTP_CAN_NOT_BE_SENT = "error.otp-can-not-be-sent";

	public static final String ERROR_YOU_HAVE_REACHED_THE_LIMIT_FOR_OTP_REQUESTS = "error.you-have-reached-the-limit-for-otp-requests";

	public static final String ERROR_TRANSACTION_ID_IS_INVALID = "error.transaction-id-is-invalid";

	public static final String ERROR_TRANSACTION_HAS_BEEN_PROCESSED = "error.transaction-has-been-processed";

	public static final String ERROR_YOU_HAVE_REACHED_LIMIT_FOR_CONFIRM_OTP = "error.you-have-reached-limit-for-otp";

	public static final String ERROR_OTP_IS_INVALID_ENTRIES_LEFT = "error.otp-is-invalid-entries-left";

	public static final String ERROR_OTP_IS_EXPIRED = "error.otp-is-expired";

	public static final String ERROR_SUBSCRIBER_IS_INVALID = "error.subscriber-is-invalid";

	public static final String ERROR_CUSTOMER_NOT_FOUND = "error.customer-not-found";

	public static final String INVALID_DATA_TYPE = "error.invalid-data-type";

	public static final String INVALID_DATA_STATUS = "error.invalid-data-status";

	public static final String ERROR_DATA_ALREADY_VERIFIED = "error.data-already-verified";

    public static final String ERROR_TEMPLATE_NOT_FOUND = "error.template-not-found";

    public static final String ERROR_DUPLICATE_PRIMARY_DATA = "error.duplicate-primary-data";

    public static final String ERROR_CUSTOMER_ALREADY_ACTIVE = "error.customer-already-active";

	public static final String LABEL_EXTEND = "label.extend";

	public static final String ERROR_PRIMARY_DATA_CAN_NOT_BE_EMPTY = "error.primary-data-can-not-be-empty";

	public static final String ERROR_EXTEND_DATA_HAS_REACH_LIMIT = "error.extend-data-has-reach-limit";

	public static final String ERROR_PRIMARY_DATA_CAN_NOT_DELETE = "error.primary-data-can-not-be-deleted";

    public static final String ERROR_DUPLICATE_PHONE = "error.duplicate-phone";

	public static final String ERROR_DUPLICATE_EMAIL = "error.duplicate-email";

	public static final String ERROR_DATA_LENGTH_EXCEEDED_LIMIT = "error.data-length-exceeded-limit";

    public static final String ERROR_START_OR_END_TIME_CAN_NOT_BE_EMPTY = "error.start-or-end-time-can-not-be-empty";

	public static final String ERROR_START_CAN_NOT_BE_GREAT_THAN_END_TIME = "error.start-can-not-be-great-than-end-time";

	public static final String ERROR_PLEASE_ENTER_EMAIL_OR_PHONE_NUMBER = "error.please-enter-email-or-phone-number";

}
