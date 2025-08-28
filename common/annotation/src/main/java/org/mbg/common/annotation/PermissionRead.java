package org.mbg.common.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify read permissions for a method or class.
 * This can be used to control access to certain resources or functionalities
 * based on defined permission levels.
 * <p>
 * Attributes:
 * - `level`: Specifies the permission level required to access the annotated resource.
 * - `allowAdministrator`: Indicates whether administrators are granted access regardless
 *   of the specified permission level. Defaults to `true`.
 * <p>
 * Targets:
 * - This annotation can be applied at the method or class level.
 * <p>
 * Retention Policy:
 * - This annotation is retained at runtime.
 * <p>
 * Framework Integration:
 * - The annotation is mapped using Spring's `@Mapping`, making it suitable
 *   for use in Spring-based applications.
 * <p>
 * Documentation:
 * - This annotation is documented to ensure visibility in generated API documentation.
 *  @author LinhLH
 *  @since 2023-10-29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface PermissionRead {
	PermissionLevel level();

	boolean allowAdministrator() default true;
}
