package org.mbg.common.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Signature} annotation is used to provide metadata at the method or type level.
 * It can help denote specific characteristics or behaviors related to the annotated element.
 * <p>
 * Targets:
 * <p>
 * - This annotation can be applied to methods or types.
 * <p>
 * Retention Policy:
 * <p>
 * - This annotation is retained at runtime, ensuring it can be accessed via reflection.
 * <p>
 * Framework Integration:
 * <p>
 * - The annotation is integrated with the Spring framework through Spring's {@code @Mapping}.
 * <p>
 * Documentation:
 * <p>
 * - This annotation is documented to ensure it appears in generated API documentation.
 *
 * @author LinhLH
 * @since 2023-10-29
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface Signature {
}
