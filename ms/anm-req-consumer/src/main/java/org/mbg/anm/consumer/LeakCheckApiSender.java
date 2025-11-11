package org.mbg.anm.consumer;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.response.LeakCheckResponse;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.api.exception.HttpResponseException;
import org.mbg.common.api.request.Request;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.GsonUtil;
import org.mbg.common.util.StringPool;
import org.mbg.common.util.StringUtil;
import org.mbg.common.util.Validator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class LeakCheckApiSender {
    private final RestTemplate restTemplate;

    private final Gson gson;

    @Retryable(retryFor = HttpResponseException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 1000L))
    public <T extends LeakCheckResponse, V extends Request> T sendToLeakCheck(V request, Class<T> clazz,
                                                                              MultiValueMap<String, String> params,
                                                                              String token) {
        HttpHeaders headers = this.getHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(request.getBaseUrl());

        HttpEntity<?> entity = null;

        if (Validator.isNotNull(params) && !params.isEmpty()) {
            builder = builder.queryParams(params);

            entity = new HttpEntity<>(headers);
        } else {
            entity = new HttpEntity<>(request, headers);
        }

        URI uri = builder.build(false).toUri();

        _log.info("Start call sendToLeakCheck with request: {}, {}", uri.toString(), this.gson.toJson(request));

        ResponseEntity<String> response = this.restTemplate.exchange(uri, request.getMethod(), entity, String.class);

        String body = response.getBody();

        _log.info("sendToLeakCheck response body: {} ", body);

        T apiResponse = GsonUtil.canJsonParse(response) ? this.gson.fromJson(body, clazz) : null;

        if (response.getStatusCode().equals(HttpStatus.OK)) {

            if (Validator.isNull(apiResponse)) {
                _log.error("An unexpected error has occurred when sendToLeakCheck: response is null ");
            }

            return apiResponse;
        } else {
            _log.error("An unexpected error has occurred when sendToLeakCheck: status: {}, body: {} ",
                    response.getStatusCode().value(), body);

            if (Validator.isNotNull(apiResponse)) {
                throw new HttpResponseException(response.getStatusCode().value(), String.valueOf(response.getStatusCode().value()),
                        Validator.isNotNull(apiResponse.getError())
                                ? StringUtil.join(Collections.singleton(apiResponse.getError()), StringPool.COMMA)
                                : StringPool.BLANK,
                        apiResponse.getError(), null);
            }

            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED),
                    LeakCheckResponse.class.getName(), LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED);
        }
    }


    private HttpHeaders getHeaders() {

        return HeaderUtil.getTypeJsonHeaders();
    }
}
