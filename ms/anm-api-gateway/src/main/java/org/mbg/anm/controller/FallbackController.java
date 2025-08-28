package org.mbg.anm.controller;

import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.anm.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Controller responsible for handling fallback requests when downstream services
 * are unavailable and their corresponding circuit breakers are open.
 * This controller provides generic error responses to the client.
 *
 * @author: LinhLH
 */
@RestController
@Slf4j
public class FallbackController {

    /**
     * Handles requests forwarded to the common fallback URI ('/fallback/service-unavailable').
     * This endpoint is typically configured as the `fallbackUri` in CircuitBreaker filters
     * within the API Gateway configuration.
     * <p>
     * It logs the original request path that triggered the fallback and returns a
     * standardized {@link HttpStatus#SERVICE_UNAVAILABLE} (503) response with a
     * generic error message retrieved from the label management system.
     * </p>
     *
     * @param exchange The current server exchange, providing access to request details like the original path.
     * @return A {@link Mono} emitting a {@link ResponseEntity} with HTTP status 503 and a generic error message body.
     */
    @GetMapping("/fallback/service-unavailable")
    public Mono<ResponseEntity<String>> commonServiceFallback(ServerWebExchange exchange) {
        // Log the fallback event. You can inspect the original request path for context.
        String originalPath = exchange.getRequest().getPath().toString();

        // FIX: Use 'log' instead of '_log' (assuming @Slf4j is used correctly)
        LogUtil.warn(_log, exchange, "[commonServiceFallback] Common fallback triggered. Original request path: {}",
                originalPath);

        // Provide a generic response using the label system
        String responseBody = Labels.getLabels(LabelKey.ERROR_SERVICE_UNAVAILABLE_TRY_AGAIN);

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseBody));
    }
}