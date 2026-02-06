package com.hust.common.api.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import com.hust.common.api.exception.BadRequestException;
import com.hust.common.base.annotation.ReqEncryptAes256;
import com.hust.common.base.annotation.ResEncryptAes256;
import com.hust.common.base.enums.ErrorCode;
import com.hust.common.security.RsaProvider;
import com.hust.common.security.SignatureProvider;
import com.hust.common.security.configuration.RsaProperties;
import com.hust.common.security.util.SecurityConstants;
import com.hust.common.util.AES256GCMUtil;
import com.hust.common.util.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ReqControllerAdvice extends RequestBodyAdviceAdapter {

    private final RsaProvider rsaProvider;

    private final Signature signature;

    private final ObjectMapper om;

    private final RsaProperties rsaProperties;

    public ReqControllerAdvice(RsaProvider rsaProvider, ObjectMapper om, RsaProperties rsaProperties)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        this.rsaProvider = rsaProvider;
        this.rsaProperties = rsaProperties;
        this.signature = Signature.getInstance("SHA256withRSA");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] encoded = Base64.getDecoder().decode(this.rsaProperties.getSignal().getPublicKey());
        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(encoded));
        this.signature.initVerify(publicKey);
        this.om = om;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();

        return method != null && AnnotatedElementUtils.hasAnnotation(method, ReqEncryptAes256.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        byte[] raw = inputMessage.getBody().readAllBytes();
        ObjectNode root = (ObjectNode) om.readTree(raw);
        byte[] body = root.get("data").asText().getBytes(StandardCharsets.UTF_8);
        byte[] combineText = Base64.getDecoder().decode(body);

        String encKey = inputMessage.getHeaders().getFirst(SecurityConstants.Header.X_ENC_KEY);
        String nonceBase64 = inputMessage.getHeaders().getFirst(SecurityConstants.Header.X_Nonce);
        byte[] nonce = Base64.getDecoder().decode(nonceBase64);

        String signature = inputMessage.getHeaders().getFirst(SecurityConstants.Header.X_SIG);

        if (Validator.isNull(signature)) {
            throw new BadRequestException(ErrorCode.MSG1047);
        }

        try {
            this.signature.update(body);

            if (!this.signature.verify(Base64.getDecoder().decode(signature))) {
                throw new BadRequestException(ErrorCode.MSG1047);
            }
        } catch (SignatureException e) {
            throw new BadRequestException(ErrorCode.MSG1047);
        }

        byte[] key;

        try {
            key = this.rsaProvider.decrypt(encKey).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.MSG1032);
        }

        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).setAttribute("AES_SESSION_KEY", key, RequestAttributes.SCOPE_REQUEST);

        if (combineText == null || combineText.length == 0) {
            return inputMessage;
        }

        byte[] plainText;

        try {
            plainText = AES256GCMUtil.decrypt(combineText, key, nonce,null);
        } catch(Exception e) {
            throw new BadRequestException(ErrorCode.MSG1032);
        }

        HttpInputMessage message = new HttpInputMessage() {
            @Override public InputStream getBody() { return new ByteArrayInputStream(plainText); }
            @Override public HttpHeaders getHeaders() {
                HttpHeaders h = new HttpHeaders();
                h.putAll(inputMessage.getHeaders());
                h.setContentLength(plainText.length);
                h.setContentType(MediaType.APPLICATION_JSON);
                return h;
            }
        };

        return super.beforeBodyRead(message, parameter, targetType, converterType);
    }
}
