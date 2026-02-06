package com.hust.common.aop;

import com.hust.common.annotation.LoginCaptcha;
import com.hust.common.annotation.RequiredCaptcha;
import com.hust.common.api.exception.BadRequestException;
import com.hust.common.api.util.ApiConstants;
import com.hust.common.api.util.HttpUtil;
import com.hust.common.configuration.CaptchaProperties;
import com.hust.common.label.LabelKey;
import com.hust.common.label.Labels;
import com.hust.common.service.CaptchaService;
import com.hust.common.service.LoginAttemptService;
import com.hust.common.util.CaptchaConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;

/**
 * Aspect for handling CAPTCHA-related logic in annotated methods.
 * <p>
 * This aspect intercepts methods annotated with specific CAPTCHA-related annotations
 * in order to validate CAPTCHA headers and enforce security measures such as rate-limiting
 * and bot detection.
 * <p>
 * It provides the logic to verify CAPTCHA values and their corresponding transaction IDs
 * against stored or generated values to ensure correctness.
 * <p>
 * The aspect works with headers defined in the system's configurations and interacts
 * with services for CAPTCHA validation and login attempt tracking.
 * <p>
 * This is particularly useful in securing endpoints that are susceptible to brute force
 * attacks or bot automation.
 *
 * @author LinhLH
 * @since 04/15/2025
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CaptchaAspect {
    private final CaptchaProperties captchaProperties;

    private final PasswordEncoder encoder;

    private final LoginAttemptService loginAttemptService;

    private final CaptchaService captchaService;

    @SuppressWarnings("unused")
    @Before("@annotation(requiredCaptcha)")
    public void requiredCaptcha(RequiredCaptcha requiredCaptcha) {
        String headerName = this.captchaProperties.getHeaderName();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_CAPTCHA),
                            CaptchaConstants.EntityName.CAPTCHA, LabelKey.ERROR_INCORRECT_CAPTCHA);
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String captcha = request.getHeader(headerName);

        String transactionId = request.getHeader(ApiConstants.HttpHeaders.X_TRANSACTION_ID);

        _log.info("[requiredCaptcha] captcha {} of transactionId {}", captcha, transactionId);

        if (Objects.isNull(captcha) || Objects.isNull(transactionId)
                        || !this.encoder.matches(captcha, transactionId)
                        || this.captchaService.isInvalidCaptcha(transactionId, captcha)) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_CAPTCHA),
                            CaptchaConstants.EntityName.CAPTCHA, LabelKey.ERROR_INCORRECT_CAPTCHA);
        }
    }

    @SuppressWarnings("unused")
    @Before("@annotation(loginCaptcha)")
    public void loginCaptcha(LoginCaptcha loginCaptcha) {
        String headerName = this.captchaProperties.getHeaderName();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_CAPTCHA),
                            CaptchaConstants.EntityName.CAPTCHA, LabelKey.ERROR_INCORRECT_CAPTCHA);
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        
        // check if the IP is not currently blocked then skip
        if (!this.loginAttemptService.isRequiredCaptcha(HttpUtil.getClientIP(request))) {
            return;
        }

        String captcha = request.getHeader(headerName);

        String transactionId = request.getHeader(ApiConstants.HttpHeaders.X_TRANSACTION_ID);

        _log.info("[loginCaptcha] captcha {} of transactionId {}", captcha, transactionId);

        if (Objects.isNull(captcha) || Objects.isNull(transactionId)
                        || !this.encoder.matches(captcha, transactionId)
                        || this.captchaService.isInvalidCaptcha(transactionId, captcha)) {
            Map<String, Object> params = this.captchaService.generateRequired();

            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_CAPTCHA),
                            CaptchaConstants.EntityName.CAPTCHA, LabelKey.ERROR_INCORRECT_CAPTCHA, params);
        }
    }
}
