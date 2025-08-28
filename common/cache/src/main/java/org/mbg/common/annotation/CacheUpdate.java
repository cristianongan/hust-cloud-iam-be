package org.mbg.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 07/04/2025 - LinhLH: Create new
 *
 * @author LinhLH
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheUpdate {
    CacheCollection[] collection() default {};
    
    CacheMap[] map() default {};
}
