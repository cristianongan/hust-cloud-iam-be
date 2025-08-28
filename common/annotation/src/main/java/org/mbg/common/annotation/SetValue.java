package org.mbg.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code SetValue} annotation is used to associate a specific target with a value.
 * This can be applied to methods or types, allowing users to define metadata for various components.
 * <p>
 * Attributes:
 * <p>
 * - `target`: Specifies the target entity to which the value is associated.
 * <p>
 * - `value`: Defines the value associated with the specified target.
 * <p>
 * Use Cases:
 * <p>
 * - This annotation can be used standalone or as part of the {@code SetValues} annotation to define multiple entries.
 * <p>
 * Retention Policy:
 * <p>
 * - The annotation is retained at runtime.
 * <p>
 * Targets:
 * <p>
 * - This annotation can be applied at the method or type level.
 * <p>
 * Documentation:
 * <p>
 * - The annotation is documented to ensure it is included in generated API documentation.
 *
 * @author LinhLH
 * @since 2023-10-29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SetValue {
	String target();
	
	String value();
}
