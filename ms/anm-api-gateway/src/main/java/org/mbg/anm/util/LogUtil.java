package org.mbg.anm.util;

import org.mbg.common.security.util.SecurityConstants;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Signal;
import reactor.util.context.ContextView;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Utility class for logging with request context information.
 * It adds the request ID to the MDC for each log entry.
 */

@UtilityClass
public class LogUtil {
    public static void info(Logger log, ServerWebExchange exchange, String format, Object... args) {
        withRequestId(exchange, () -> log.info(format, args));
    }

    public static void debug(Logger log, ServerWebExchange exchange, String format, Object... args) {
        withRequestId(exchange, () -> log.debug(format, args));
    }

    public static void warn(Logger log, ServerWebExchange exchange, String format, Object... args) {
        withRequestId(exchange, () -> log.warn(format, args));
    }

    public static void error(Logger log, ServerWebExchange exchange, String format, Object... args) {
        withRequestId(exchange, () -> log.error(format, args));
    }

    public static void trace(Logger log, ServerWebExchange exchange, String format, Object... args) {
        withRequestId(exchange, () -> log.trace(format, args));
    }

    /**
     * Creates a consumer that sets up MDC context from the Reactor context before executing an action.
     *
     * @param key The key for the MDC entry
     * @param logFunction The logging function to execute with MDC context set
     * @return A consumer that properly sets up MDC context
     */
    public static <T> Consumer<Signal<T>> logOnNext(String key, Consumer<T> logFunction) {
        return signal -> {
            if (signal.hasValue()) {
                Optional.of(signal.getContextView().getOrEmpty(key))
                        .ifPresent(value -> {
                            try {
                                MDC.put(key, value.toString());
                                logFunction.accept(signal.get());
                            } finally {
                                MDC.remove(key);
                            }
                        });
            }
        };
    }

    /**
     * Creates a consumer that sets up MDC context for logging at the completion of a reactive stream.
     *
     * @param key The key for the MDC entry
     * @param value The value to set in the MDC
     * @param logAction The logging action to execute with MDC context set
     * @return A consumer that properly sets up MDC context for completion signals
     */
    public static Consumer<Signal<?>> logOnComplete(String key, String value, Runnable logAction) {
        return signal -> {
            if (signal.isOnComplete() || signal.isOnError()) {
                try {
                    MDC.put(key, value);
                    logAction.run();
                } finally {
                    MDC.remove(key);
                }
            }
        };
    }

    /**
     * Executes a logging action with MDC context set.
     *
     * @param key The key for the MDC entry
     * @param value The value to set in the MDC
     * @param logAction The logging action to execute with MDC context set
     */
    public static void withMdc(String key, String value, Runnable logAction) {
        try {
            MDC.put(key, value);
            logAction.run();
        } finally {
            MDC.remove(key);
        }
    }

    /**
     * Executes a logging action with MDC context set using the requestId from the exchange.
     *
     * @param exchange The ServerWebExchange containing the requestId
     * @param logAction The logging action to execute with MDC context set
     */
    public static void withRequestId(ServerWebExchange exchange, Runnable logAction) {
        String requestId = extractRequestId(exchange);
        if (requestId != null) {
            withMdc(SecurityConstants.Header.REQUEST_ID_MDC, requestId, logAction);
        } else {
            logAction.run();
        }
    }

    /**
     * Creates a consumer that sets up MDC context for logging using the requestId from the exchange.
     *
     * @param exchange The ServerWebExchange containing the requestId
     * @param logAction The logging action to execute with MDC context set
     * @return A consumer that properly sets up MDC context for completion signals
     */
    public static Consumer<Signal<?>> logOnCompleteWithRequestId(ServerWebExchange exchange, Runnable logAction) {
        String requestId = extractRequestId(exchange);

        return logOnComplete(SecurityConstants.Header.REQUEST_ID_MDC, requestId, logAction);
    }

    /**
     * Extracts the requestId from the ServerWebExchange.
     *
     * @param exchange The ServerWebExchange containing the requestId
     * @return The requestId or null if not found
     */
    public static String extractRequestId(ServerWebExchange exchange) {
        if (exchange == null) {
            return null;
        }

        // Try to get from attributes first (set by the RequestLoggingFilter)
        String requestId = (String) exchange.getAttribute(SecurityConstants.Header.X_REQUEST_ID);

        // If not in attributes, check headers
        if (requestId == null) {
            requestId = exchange.getRequest().getHeaders().getFirst(SecurityConstants.Header.X_REQUEST_ID);
        }

        return requestId;
    }

    /**
     * Gets the request ID from the context, falling back to a new UUID if not present.
     * Uses the correct key to match what's set in the RequestLoggingFilter.
     *
     * @param context The reactor context
     * @return The request ID
     */
    private String getRequestId(ContextView context) {
        // Use the same key as set in the RequestLoggingFilter
        return context.getOrDefault(SecurityConstants.Header.REQUEST_ID_MDC, UUID.randomUUID().toString());
    }
}