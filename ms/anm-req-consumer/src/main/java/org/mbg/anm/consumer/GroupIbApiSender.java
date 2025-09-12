package org.mbg.anm.consumer;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.exception.GroupIbTooManyReq;
import org.mbg.anm.consumer.response.GroupIbResponse;
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
public class GroupIbApiSender {
    private final RestTemplate restTemplate;

    private final Gson gson;

    @Retryable(retryFor = GroupIbTooManyReq.class, maxAttempts = 2,
            backoff = @Backoff(delay = 1500L))
    public <T extends GroupIbResponse, V extends Request> T sendToGroupIb(V request, Class<T> clazz,
                                                                          MultiValueMap<String, String> params,
                                                                          String username, String password) {
        HttpHeaders headers = this.getHeaders(username, password);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(request.getBaseUrl());

        HttpEntity<?> entity = null;

        if (Validator.isNotNull(params) && !params.isEmpty()) {
            builder = builder.queryParams(params);

            entity = new HttpEntity<>(headers);
        } else {
            entity = new HttpEntity<>(request, headers);
        }

        URI uri = builder.build(false).toUri();

        _log.info("Start call sendToGroupIb with request: {}, {}", uri.toString(), this.gson.toJson(request));

        ResponseEntity<String> response = this.restTemplate.exchange(uri, request.getMethod(), entity, String.class);

        String body = response.getBody();

        _log.info("sendToGroupIb response body: {} ", body);

        T apiResponse = GsonUtil.canJsonParse(response) ? this.gson.fromJson(body, clazz) : null;

        if (response.getStatusCode().equals(HttpStatus.OK)) {

            if (Validator.isNull(apiResponse)) {
                _log.error("An unexpected error has occurred when sendToGroupIb: response is null ");
            }

            return apiResponse;
        } else {
            _log.error("An unexpected error has occurred when sendToGroupIb: status: {}, body: {} ",
                    response.getStatusCode().value(), body);

            if (Validator.isNotNull(apiResponse)) {
                if (Validator.equals(apiResponse.getCode(), HttpStatus.TOO_MANY_REQUESTS.value())) {
                    _log.error("sendToGroupIb TOO_MANY_REQUESTS");

                    throw new GroupIbTooManyReq();
                }

                throw new HttpResponseException(response.getStatusCode().value(), String.valueOf(apiResponse.getCode()),
                        Validator.isNotNull(apiResponse.getMessage())
                                ? StringUtil.join(Collections.singleton(apiResponse.getMessage()), StringPool.COMMA)
                                : StringPool.BLANK,
                        apiResponse.getMessage(), null);
            }

            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED),
                    GroupIbResponse.class.getName(), LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED);
        }
    }


    private HttpHeaders getHeaders(String username, String password) {
        HttpHeaders headers = HeaderUtil.getTypeJsonHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, HeaderUtil.getBasicAuthorization(username, password));

        return headers;
    }

}
