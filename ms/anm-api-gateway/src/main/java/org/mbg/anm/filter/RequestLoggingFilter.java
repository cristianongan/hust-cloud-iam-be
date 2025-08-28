package org.mbg.anm.filter;

import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.StringUtil;
import org.mbg.anm.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * A Spring {@code GlobalFilter} implementation that logs incoming HTTP requests and outgoing HTTP responses.
 * This filter attaches a unique {@code X-Request-Id} identifier to each request and response to enable
 * consistent tracking across the application.
 * <p>
 * The filter performs the following primary operations:
 * - Extracts or generates a unique {@code X-Request-Id} for the request.
 * - Adds the {@code X-Request-Id} to the {@code ServerWebExchange}'s attributes and response headers.
 * - Puts the {@code X-Request-Id} in the MDC for logging purposes during the request lifecycle.
 * - Logs the HTTP method and path of the request when it is processed.
 * - Logs the HTTP method, path, and response status after the response is issued.
 * - Removes the {@code X-Request-Id} from the MDC once the response cycle is completed.
 * <p>
 * This filter is executed with the highest precedence to ensure that request logging occurs as early
 * as possible in the filter chain.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements GlobalFilter {

    /**
     * Filters incoming HTTP requests and outgoing HTTP responses, attaching a unique X-Request-Id
     * identifier for consistent request tracking. This method performs the following actions:
     * 1. Retrieves or generates a unique X-Request-Id value.
     * 2. Adds the X-Request-Id to the request attributes and response headers.
     * 3. Adds the X-Request-Id to the MDC (Mapped Diagnostic Context) for logging purposes.
     * 4. Logs the HTTP method and path of the incoming request.
     * 5. Logs the HTTP method, path, and response status after the response is issued.
     * 6. Ensures the X-Request-Id is removed from the MDC after the response cycle ends.
     *
     * @param exchange the {@code ServerWebExchange} representing the current HTTP request and response
     * @param chain the {@code GatewayFilterChain} used to delegate to the next filter in the chain
     * @return a {@code Mono<Void>} that completes when the request and response processing is complete
     */
    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getResponse().getHeaders();

        String requestId = getRequestIdFromHeader(httpHeaders);

        exchange.getAttributes().put(SecurityConstants.Header.X_REQUEST_ID, requestId);

        httpHeaders.add(SecurityConstants.Header.X_REQUEST_ID, requestId);

        MDC.put(SecurityConstants.Header.REQUEST_ID_MDC, requestId);

        LogUtil.info(_log, exchange, "Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getPath().value());

        return chain.filter(exchange)
                .contextWrite(context -> context.put(SecurityConstants.Header.REQUEST_ID_MDC, requestId))
                .doFinally(signalType -> {
                    LogUtil.info(_log, exchange, "Response: {} {} - Status: {}", exchange.getRequest().getMethod(),
                            exchange.getRequest().getPath().value(), exchange.getResponse().getStatusCode());

                    MDC.remove(SecurityConstants.Header.REQUEST_ID_MDC);
                });
    }

    /**
     * Retrieves the X-Request-Id value from the provided HTTP headers. If no value is found or the retrieved
     * value is empty, a new UUID is generated and returned.
     *
     * @param httpHeaders the {@code HttpHeaders} object containing the HTTP request headers
     * @return the X-Request-Id value from the headers, or a newly generated UUID if not present
     */
    private String getRequestIdFromHeader(HttpHeaders httpHeaders) {
        String requestId = httpHeaders.getFirst(SecurityConstants.Header.X_REQUEST_ID);

        if (StringUtil.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        return requestId;
    }
}