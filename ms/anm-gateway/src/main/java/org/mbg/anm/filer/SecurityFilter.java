package org.mbg.anm.filer;

import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.configuration.ClientApiProperties;
import org.mbg.common.base.model.dto.response.VerifyRes;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.Validator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

@Slf4j
public class SecurityFilter implements WebFilter {
    private final WebClient.Builder webClientBuilder;

    private final ClientApiProperties clientApiProperties;

    private WebClient webClient;

    public SecurityFilter(WebClient.Builder webClientBuilder, ClientApiProperties clientApiProperties) {
        this.webClientBuilder = webClientBuilder;
        this.clientApiProperties = clientApiProperties;
        this.webClient = this.webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String auth = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        _log.info("SecurityFilter req: {}" , req.getPath().toString() );

        if (Validator.isNull(auth)) {
            return chain.filter(exchange);
        }
        return webClientBuilder.build().get()
                .uri(this.clientApiProperties.getVerifyToken())
                .header(HttpHeaders.AUTHORIZATION, auth)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .flatMap(b -> Mono.error(new ResponseStatusException(r.statusCode(), b))))
                .bodyToMono(VerifyRes.class)

                .flatMap(user -> {
                    var authn = new UsernamePasswordAuthenticationToken(
                            user.getUser(), "N/A", user.getPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                    var mutatedReq = req.mutate()
                            .header(SecurityConstants.Header.USER, user.getUser())
                            .header(SecurityConstants.Header.X_SERVICE_PERMISSIONS, String.join(",", user.getPermissions()))
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedReq).build())
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authn));
                })

                .onErrorResume(ResponseStatusException.class, e -> {
                    exchange.getResponse().setStatusCode(e.getStatusCode());
                    return exchange.getResponse().setComplete();
                })
                .timeout(Duration.ofMillis(10000),
                        Mono.defer(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                            return exchange.getResponse().setComplete();
                        }));
    }
}
