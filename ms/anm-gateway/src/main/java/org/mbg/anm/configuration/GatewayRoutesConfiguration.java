package org.mbg.anm.configuration;

import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.mbg.anm.util.GatewayConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = GatewayConstant.PropertyPrefix.GATEWAY_CONFIG)
@Configuration
public class GatewayRoutesConfiguration {
    private List<RouteDefinition> routes = new ArrayList<>();

    @Getter
    @Setter
    public static class RouteDefinition {
        private String id;

        private String path;

        private String uri;
        // Optional: Default to 0 if not specified in YAML
        private int stripPrefix = 0;

        private boolean circuitBreakerEnabled;

        private String circuitBreakerName;

        private boolean tokenRelayEnabled;

        private Map<String, Object> metadata = new HashMap<>();
    }

    @Bean
    @LoadBalanced
    WebClient.Builder lbWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    HttpClientCustomizer longTimeouts() {
        return httpClient -> httpClient
                .responseTimeout(Duration.ofSeconds(300))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000);
    }

    /**
     * Defines the route for the product-service.
     * Requests matching the path predicate will be forwarded to the product-service instance
     * discovered via the load balancer (e.g., Eureka), after stripping the prefix.
     *
     * @param builder The RouteLocatorBuilder provided by Spring Cloud Gateway to define routes.
     * @return A RouteLocator bean containing the route definitions.
     */
    @Bean
    public RouteLocator gatewayServiceRoute(RouteLocatorBuilder builder) {
        // Start building routes
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();

        // Check if routes are defined in properties
        if (CollectionUtils.isEmpty(this.routes)) {
            _log.warn("[gatewayServiceRoute] No custom gateway routes defined under 'app.gateway.routes'. " +
                    "No routes will be configured dynamically.");
            // Return an empty locator or a default one if needed
            return routesBuilder.build();
        }

        _log.info("[gatewayServiceRoute] Loading {} custom gateway routes from configuration.", this.routes.size());

        // Iterate over each route definition from properties
        this.routes.forEach(routeDef -> {
            _log.debug("[gatewayServiceRoute] Configuring route: id='{}', path='{}', uri='{}', stripPrefix={}",
                    routeDef.getId(), routeDef.getPath(), routeDef.getUri(), routeDef.getStripPrefix());

            // Add a route for each definition
            routesBuilder.route(routeDef.getId(), r -> r.path(routeDef.getPath())
                    .and().method(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                    .filters(f -> {
                        f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST");
                        f.dedupeResponseHeader("Access-Control-Allow-Credentials", "RETAIN_FIRST");
                        f.dedupeResponseHeader("Access-Control-Expose-Headers", "RETAIN_FIRST");
                        f.dedupeResponseHeader("Vary", "RETAIN_FIRST");

                        // 1. Apply stripPrefix if configured
                        if (routeDef.getStripPrefix() > 0) {
                            _log.trace("[gatewayServiceRoute] Applying stripPrefix({}) filter for route '{}'",
                                    routeDef.getStripPrefix(), routeDef.getId());
                            f.stripPrefix(routeDef.getStripPrefix());
                        }

                        // 2. APPLY TOKENRELAY FILTER IF ENABLED
                        if (routeDef.isTokenRelayEnabled()) {
                            _log.trace("[gatewayServiceRoute] Applying TokenRelay filter for route '{}'", routeDef.getId());

                            f.tokenRelay();
                        }

                        // 3. Apply Circuit Breaker if enabled for this route
                        if (routeDef.isCircuitBreakerEnabled()) {
                            String cbName = Optional.ofNullable(routeDef.getCircuitBreakerName())
                                    .filter(name -> !name.isBlank())
                                    .orElse(routeDef.getId() + "_cb");

                            _log.trace("[gatewayServiceRoute] Applying CircuitBreaker filter '{}' with common fallback for route '{}'",
                                    cbName, routeDef.getId());

                            f.circuitBreaker(config -> config
                                    .setName(cbName)
                                    .setFallbackUri(GatewayConstant.Uri.COMMON_FALLBACK_URI));
                        }

                        // 4. Apply Metadata
                        if (MapUtils.isNotEmpty(routeDef.getMetadata())) {
                            _log.trace("[gatewayServiceRoute] Applying metadata for route '{}': {}",
                                    routeDef.getId(), routeDef.getMetadata());
                            f.metadata(routeDef.getMetadata());
                        } else {
                            _log.trace("[gatewayServiceRoute] No metadata configured for route '{}'", routeDef.getId());
                        }

                        return f;
                    }).uri(routeDef.getUri()));
        });

        _log.info("[gatewayServiceRoute] Successfully configured custom gateway routes.");
        // Build and return the final RouteLocator
        return routesBuilder.build();
    }
}
