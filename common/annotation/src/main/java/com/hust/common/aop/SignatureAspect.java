package com.hust.common.aop;

import com.hust.common.api.exception.BadRequestException;
import com.hust.common.api.util.ApiConstants;
import com.hust.common.base.enums.ErrorCode;
import com.hust.common.security.RsaProvider;
import com.hust.common.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
//@RequiredArgsConstructor
public class SignatureAspect {
    @Qualifier("signalRsaProvider")
	private RsaProvider signalRsaProvider;

    SignatureAspect() {
        _log.info("SignatureAspect start");
    }

	@Before("@annotation(com.hust.common.base.annotation.Signature)")
	public void requiredKaptcha() throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new BadRequestException(ErrorCode.MSG1047);
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String signature = request.getHeader(ApiConstants.HttpHeaders.X_SIGNATURE);

        String body = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));


        if (Validator.isNull(signature) || Validator.isNull(body)) {
            throw new BadRequestException(ErrorCode.MSG1047);
        }




	}
}
