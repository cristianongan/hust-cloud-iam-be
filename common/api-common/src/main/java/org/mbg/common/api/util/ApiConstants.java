package org.mbg.common.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;

/**
 * Utility class defining constants commonly used across APIs.
 * This class cannot be instantiated. Uses Lombok's {@link NoArgsConstructor}
 * to generate a private constructor.
 * It serves as a container for nested classes that organize these constants
 * based on their context or purpose (e.g., Entity Names, Error Codes, Headers).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {

    /**
     * Constants representing entity names.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EntityName {
        public static final String ACCOUNT = "account";
        public static final String SIGNATURE = "signature";
        public static final String USER = "user";
    }

    /**
     * Constants representing basic success/failure codes.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ErrorCode {
        public static final int FAILURE = 2;
        public static final int SUCCESS = 1;
    }

    /**
     * Constants representing keys commonly used in error responses (e.g., Problem Details).
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ErrorKey {
        public static final String FIELD_ERRORS = "fieldErrors";
        public static final String MESSAGE = "message";
        public static final String ERROR_CODE = "errorCode";
        public static final String DATA = "data";
        public static final String ERROR_KEY = "errorKey";
        public static final String PARAMS = "params";
        public static final String PATH = "path";
        public static final String VIOLATIONS = "violations";
        public static final String REASON_CODE = "reasonCode";
    }

    /**
     * Constants representing specific error type URIs for Problem Details responses.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ErrorType {
        public static final String PROBLEM_BASE_URL = "org-mbg-anm";
        public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
        public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
        public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
        public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
        public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
        public static final URI PHONE_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/phone-already-used");
        public static final URI APPOINTMENT_TIME_AVAILABLE_TYPE = URI.create(PROBLEM_BASE_URL + "/appointment-time-used");
    }

    /**
     * Constants representing common HTTP Header names and related values.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpHeaders {
        public static final String LINK_FORMAT = "<{0}>; rel=\"{1}\"";
        public static final String X_TRANSACTION_ID = "X-TRANSACTION-ID";
        public static final String X_SIGNATURE = "X-SIGNATURE";
        public static final String X_ACTION_MESSAGE = "X-Action-Message";
        public static final String X_ACTION_PARAMS = "X-Action-Params";
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String X_TOTAL_COUNT = "X-Total-Count";
        public static final String SIGNATURE = "Signature";
        public static final String TRANSACTION_ID = "transactionId";

        /**
         * Constants specific to Payment Hub integration headers.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class PaymentHub {
            public static final String MERCHANT_CODE = "MERCHANT_CODE";
            public static final String MERCHANT_SECRET = "MERCHANT_SECRET";
        }

        /**
         * Constants specific to Apigee integration headers.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class ApiGee {
            public static final String CLIENT_MESSAGE_ID = "ClientMessageId";
        }
    }

    /**
     * Constants related to pagination parameters and link relations.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Pagination {
        public static final String NEXT = "next";
        public static final String PREV = "prev";
        public static final String LAST = "last";
        public static final String FIRST = "first";
        public static final String PAGE = "page";
        public static final String SIZE = "size";
    }
}