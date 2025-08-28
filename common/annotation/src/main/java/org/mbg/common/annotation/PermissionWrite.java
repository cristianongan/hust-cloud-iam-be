package org.mbg.common.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify write permissions for a method or class.
 * <p>
 * This can be used to control access to certain resources or functionalities
 * requiring write-level permissions based on defined permission levels.
 *
 * <p>
 * Attributes:
 * <p>
 * - `level`: Specifies the permission level required to perform write operations on the annotated resource.
 * <p>
 * - `allowAdministrator`: Indicates whether administrators are granted write access regardless of the specified
 *   permission level. Defaults to `true`.
 *
 * <p>
 * Targets:
 * <p>
 * - This annotation can be applied at the method or class level.
 *
 * <p>
 * Retention Policy:
 * <p>
 * - This annotation is retained at runtime.
 *
 * <p>
 * Framework Integration:
 * <p>
 * - The annotation is mapped using Spring's `@Mapping`, making it suitable for use in Spring-based applications.
 *
 * <p>
 * Documentation:
 * <p>
 * - This annotation is documented to ensure visibility in generated API documentation.
 *
 * <p>
 * @author LinhLH
 * @since 2023-10-29
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface PermissionWrite {
	PermissionLevel level();
	
	boolean allowAdministrator() default true;
}
