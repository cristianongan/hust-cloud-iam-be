package com.hust.common.api.exception;

import lombok.Getter;
import com.hust.common.api.enums.ClientResponseError;
import org.zalando.problem.AbstractThrowableProblem;

import java.io.Serial;

@Getter
public class ClientResponseException extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = -3435270795345773211L;

//    private final String reasonCode;
//
//    private final String message;

    public ClientResponseException(String reasonCode, String message) {
//        this.reasonCode = reasonCode;
//        this.message = message;
    }

    public ClientResponseException(ClientResponseError error) {
//        this.reasonCode = error.getCode();
//        this.message = Labels.getLabels(error.getMessage());
    }
}
