package com.hust.iam.controller;

import lombok.extern.slf4j.Slf4j;
import com.hust.iam.util.LogUtil;
import com.hust.common.label.LabelKey;
import com.hust.common.label.Labels;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class FallbackController {
    @RequestMapping("/fallback/service-unavailable")
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
