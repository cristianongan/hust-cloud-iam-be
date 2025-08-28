package org.mbg.common.annotation;

import org.mbg.common.util.StringPool;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LinhLH
 * @since 2023-10-29
 * This annotation is used to provide permission-related metadata for methods or types.
 * It allows specifying a prefix to be associated with the permission data.
 * <p>
 * - The `prefix` attribute can be used to define a custom string prefix, which defaults to an empty string.
 * <p>
 * The annotation can be applied at the method or type level and is retained at runtime.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface PermissionData {

	String prefix() default StringPool.BLANK;
}
