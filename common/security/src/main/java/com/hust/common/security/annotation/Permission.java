package com.hust.common.security.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: LinhLH
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface Permission {
    String value();
}
