package org.mbg.common.security.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {

    /** The Constant serialVersionUID */
    @Serial
    private static final long serialVersionUID = -254654794661545616L;

    private final String tokenType;
    private final String token;
    private final String message;
}
