package org.mbg.common.aop;

import org.mbg.common.annotation.SetValue;
import org.mbg.common.annotation.SetValues;
import org.mbg.common.util.Validator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect class responsible for handling the {@code SetValue} and {@code SetValues} annotations to dynamically
 * manipulate data within the application. It uses Spring AOP and SpEL (Spring Expression Language) to evaluate
 * and set values dynamically based on the specified targets and values in the annotations.
 * <p>
 * The primary purpose of this aspect is to provide a declarative way to assign values to specified targets at runtime,
 * reducing the need for boilerplate code in components where such dynamic assignment logic is required.
 * <p>
 * Key Features:
 * <p>
 * - Intercepts methods annotated with {@code SetValue} or {@code SetValues}.
 * - Uses SpEL for evaluating the target and value attributes.
 * - Provides a method to create SpEL evaluation contexts to support value resolution.
 * <p>
 * Dependencies:
 * <p>
 * - Uses Spring AOP for method interception.
 * - Employs SpEL (Spring Expression Language) for dynamic evaluation of expressions.
 * - Integrates with Spring's dependency injection for required components.
 *
 * @author LinhLH
 * @since 2023-10-29
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SetValueAspect {

    /**
     * Aspect class responsible for handling the {@code SetValue} and {@code SetValues} annotations to dynamically
     * manipulate data within the application. It uses Spring AOP and SpEL (Spring Expression Language) to evaluate
     * and set values dynamically based on the specified targets and values in the annotations.
     * <p>
     * The primary purpose of this aspect is to provide a declarative way to assign values to specified targets at runtime,
     * reducing the need for boilerplate code in components where such dynamic assignment logic is required.
     * <p>
     * Key Features:
     * <p>
     * - Intercepts methods annotated with {@code SetValue} or {@code SetValues}.
     * - Uses SpEL for evaluating the target and value attributes.
     * - Provides a method to create SpEL evaluation contexts to support value resolution.
     * <p>
     * Dependencies:
     * <p>
     * - Uses Spring AOP for method interception.
     * - Employs SpEL (Spring Expression Language) for dynamic evaluation of expressions.
     * - Integrates with Spring's dependency injection for required components.
     *
     * @author LinhLH
     * @since 2023-10-29
     */
	@Before("@annotation(setValue)")
	public void setValue(final JoinPoint joinPoint, SetValue setValue) {
		_log.debug("setValue has been called");
		
		String target = setValue.target();
		
		String value = setValue.value();
		
		ExpressionParser parser = new SpelExpressionParser();
		
		// create context
		StandardEvaluationContext context = this.getEvaluationContext(joinPoint.getTarget(), joinPoint);
		
		// get value object
		Object valueObject = parser.parseExpression(value).getValue(context);
		
		// set value to target
		parser.parseExpression(target).setValue(context, valueObject);
	}

    /**
     * Aspect class responsible for handling the {@code SetValues} annotation to dynamically
     * manipulate and assign values to specific targets using SpEL (Spring Expression Language).
     * <p>
     * This aspect intercepts methods annotated with {@code SetValues} and iterates over each
     * {@code SetValue} element to dynamically assign values as specified in the annotation.
     * <p>
     * By leveraging Spring AOP and SpEL, it provides a declarative and reusable approach to
     * manage the dynamic value assignment logic at runtime.
     * <p>
     * Dependencies:
     * <p>
     * - Spring AOP for method interception
     * - SpEL (Spring Expression Language) for runtime expression evaluation
     *
     * @author LinhLH
     * @since 2023-10-29
     */
	@Before("@annotation(setValues)")
	public void setValues(final JoinPoint joinPoint, SetValues setValues) {
		_log.debug("setValues has been called");

		SetValue[] elements = setValues.elements();
		
		for (SetValue ele: elements) {
            this.setValue(joinPoint, ele);
        }
	}

    /**
     * Aspect of Project
     * Use to log resource and service
     * <p>
     * Handles evaluation context creation specific to target class and join point details.
     * This utility method generates a {@link StandardEvaluationContext} with relevant context
     * variables such as root object, target, and method parameters for further evaluation.
     * <p>
     *
     * @author LinhLH
     * @since October 2023
     */
	private StandardEvaluationContext getEvaluationContext(Object targetClass, 
			final JoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext(targetClass);

        // set return value to variables context
        context.setVariable("root", targetClass);

        MethodSignature sign = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = sign.getParameterNames();

        Object[] args = joinPoint.getArgs();
        
        // get join point target class name 
        
        context.setVariable("target", joinPoint.getTarget());

        // add parameters to variables context
        if (Validator.isNotNull(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];

                if (i < args.length) {
                    context.setVariable(parameterName, args[i]);
                }
            }
        }

        return context;
    }
}
