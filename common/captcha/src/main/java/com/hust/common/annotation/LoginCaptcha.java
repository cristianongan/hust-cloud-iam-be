package com.hust.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method requires captcha validation specifically for login-related operations.
 * This annotation enforces captcha verification as an additional security layer to prevent unauthorized or automated login attempts.
 * <p>
 * This annotation should be applied solely at the method level to ensure secure implementation for login actions.
 *
 * @author LinhLH
 * @since 04/15/2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCaptcha {

}
