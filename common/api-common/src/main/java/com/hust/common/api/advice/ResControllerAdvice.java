package com.hust.common.api.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.common.api.exception.BadRequestException;
import com.hust.common.base.annotation.ReqEncryptAes256;
import com.hust.common.base.annotation.ResEncryptAes256;
import com.hust.common.base.enums.ErrorCode;
import com.hust.common.security.RsaProvider;
import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.AES256GCMUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ResControllerAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper om;

    private final RsaProvider rsaProvider;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();

        return method != null && AnnotatedElementUtils.hasAnnotation(method, ResEncryptAes256.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        byte[] kReq = (byte[]) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
                .getAttribute("AES_SESSION_KEY", RequestAttributes.SCOPE_REQUEST);

        if (kReq == null) {
            String encKey = request.getHeaders().getFirst(SecurityConstants.Header.X_ENC_KEY);
            try {
                kReq = this.rsaProvider.decrypt(encKey).getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new BadRequestException(ErrorCode.MSG1032);
            }
        }

        if (kReq.length != 32) {
            return body;
        }

        String nonceBase64 = request.getHeaders().getFirst(SecurityConstants.Header.X_Nonce);
        byte[] nonce = Base64.getDecoder().decode(nonceBase64);

        try {
            ObjectNode root = (ObjectNode) om.valueToTree(body);

            byte[] plainBytes;
            if (body == null) {
                plainBytes = new byte[0];
            } else if (body instanceof byte[]) {
                plainBytes = root.get("result").binaryValue();
            } else if (body instanceof CharSequence) {
                plainBytes = body.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            } else {
                // JSON
                plainBytes = om.writeValueAsBytes(body);
            }

            byte[] combined = AES256GCMUtil.encrypt(plainBytes, kReq, nonce,null);

            String base64 = Base64.getEncoder().encodeToString(combined);
            root.put("result", base64);

            return om.valueToTree(root);

        } catch (Exception e) {
            throw new RuntimeException("Encrypt response error", e);
        }
    }
}
