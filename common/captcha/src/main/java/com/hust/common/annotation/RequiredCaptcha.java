package com.hust.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method requires a captcha validation before execution.
 * This annotation ensures that the annotated method integrates captcha verification as a security mechanism to prevent unwanted access or automated abuse.
 * <p>
 * This annotation should be applied at the method level in contexts where added security via captcha is necessary.
 *
 * @author LinhLH
 * @since 04/15/2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredCaptcha {
}
