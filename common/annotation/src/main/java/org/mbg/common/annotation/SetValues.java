package org.mbg.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code SetValues} annotation is used to define an array of {@link SetValue} annotations.
 * This allows grouping multiple {@code SetValue} entries together, which can then be applied
 * to methods or types for defining metadata configurations.
 * <p>
 * Use Cases:
 * <p>
 * - The annotation provides a convenient way to aggregate multiple {@code SetValue}
 * annotations into a single declaration, simplifying scenarios where multiple targets and
 * associated values need to be defined.
 * <p>
 * Retention Policy:
 * <p>
 * - This annotation is retained at runtime, ensuring accessibility via reflection at runtime.
 * <p>
 * Targets:
 * <p>
 * - This annotation can be applied at the method or type level.
 * <p>
 * Framework Integration:
 * <p>
 * - The annotation can be utilized in any Java application that supports runtime annotations,
 * and its usage is enhanced when integrated with frameworks like Spring.
 * <p>
 * Documentation:
 * <p>
 * - The annotation is documented to appear in API documentation for better visibility
 * and usability for developers.
 *
 * @author LinhLH
 * @since 2023-10-29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SetValues {
	SetValue[] elements();
}
