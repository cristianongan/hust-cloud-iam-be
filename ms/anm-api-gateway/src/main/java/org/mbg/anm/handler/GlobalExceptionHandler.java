package org.mbg.anm.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mbg.common.security.exception.UnauthorizedException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serial;
import java.io.Serializable;

/**
 * GlobalExceptionHandler handles exceptions globally within a WebFlux application.
 * It implements the ErrorWebExceptionHandler interface and is registered as a
 * Spring configuration bean with a specified order to ensure proper exception handling.
 * <p>
 * This class captures exceptions thrown during request processing, constructs a
 * standardized error response, and writes this response as JSON to the HTTP response body.
 * The error response includes the HTTP status code, error description,
 * and a user-friendly error message for the client.
 * <p>
 * Key responsibilities:
 * - Determine the HTTP status code for a given exception using the getHttpStatus method.
 * - Create an ErrorModel object to structure error response data.
 * - Write the error response as a JSON representation using an ObjectMapper.
 * <p>
 * An UnauthorizedException specifically maps to an HTTP 401 Unauthorized status,
 * while other exceptions default to an HTTP 500 Internal Server Error status.
 */
@Configuration
@RequiredArgsConstructor
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    /**
     * Handles exceptions by constructing an error response and writing it to the HTTP response body.
     * Converts the exception to an appropriate HTTP status code, creates a standardized error model,
     * and serializes it as JSON using an ObjectMapper.
     *
     * @param exchange the current server exchange containing the HTTP request and response
     * @param ex       the exception to be handled
     * @return a Mono that completes when the error response has been written to the HTTP response body
     */
    @NonNull
    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

        HttpStatus status = getHttpStatus(ex);

        ErrorModel errorModel = ErrorModel.builder()
                .httpStatus(status.value())
                .error(status.getReasonPhrase())
                .errorMessage(ex.getMessage())
                .build();

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(objectMapper.writeValueAsBytes(errorModel))));
    }

    /**
     * Determines the appropriate HTTP status code for a given exception.
     * <p>
     * If the throwable is an instance of UnauthorizedException, it returns
     * HTTP 401 Unauthorized. For all other exceptions, it defaults to
     * HTTP 500 Internal Server Error.
     *
     * @param ex the exception to evaluate
     * @return the corresponding HTTP status code
     */
    private HttpStatus getHttpStatus(Throwable ex) {
        if (ex instanceof UnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Represents a standardized error model used for constructing and
     * serializing error responses in the application.
     * <p>
     * This record is designed to encapsulate key information about an error,
     * including the HTTP status code, error identifier, and a descriptive
     * error message. It can be serialized and transferred as part of
     * HTTP responses to clients.
     * <p>
     * Fields:
     * - httpStatus: Represents the HTTP status code associated with the error.
     * - error: A short identifier or code for the type of error.
     * - errorMessage: A detailed description of the error, providing additional
     *   context about what went wrong.
     * <p>
     * The ErrorModel is used in scenarios where standardized error responses
     * are required, typically in exception handling mechanisms that generate
     * error messages in response to runtime failures or invalid states in the
     * application.
     * <p>
     * This model implements Serializable for compatibility with Java's
     * serialization mechanism in distributed systems or caching contexts,
     * ensuring that instances can be serialized and deserialized as needed.
     */
    @Builder
    public record ErrorModel (int httpStatus, String error, String errorMessage) implements Serializable {
        @Serial
        private static final long serialVersionUID = -3173266024788499987L;
    }
}