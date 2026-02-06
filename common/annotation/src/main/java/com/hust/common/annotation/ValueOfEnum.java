package com.hust.common.annotation;

import com.hust.common.aop.validator.ValueOfEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validate enum value
 *
 * @author hieu.daominh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    /**
     * Enumeration type
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * Error message
     *
     * @return String
     */
    String message() default "must be any of enum {enumClass}";

    /**
     * Name methods to get enumeration values
     */
    String methodOfValues() default "getValues";

    /**
     * Type của enum [KEY_VALUE | STRING_NAME], với type KEY_VALUE cần có methodOfValues để read value
     *
     * @return EnumType
     */
    EnumType enumType() default EnumType.KEY_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}