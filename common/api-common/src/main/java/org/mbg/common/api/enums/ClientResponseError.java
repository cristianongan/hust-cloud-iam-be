package org.mbg.common.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mbg.common.label.Labels;

@Getter
@RequiredArgsConstructor
public enum ClientResponseError {
    OK("0", ""),
    CUSTOMER_NOT_FOUND("1", "Customer not found"),
    UNAUTHORIZED("2", "Unauthorized"),
    INVALID_TOKEN("3", "Invalid token"),
    INVALID_SUBSCRIBER_ID("4", "INVALID SUBSCRIBER ID"),
    ;

    private final String code;

    private final String message;


}
