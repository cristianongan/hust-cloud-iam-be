package org.mbg.common.aop;

import org.mbg.common.util.AnnotationConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging execution of service and controller methods.
 * <p>
 * This class uses Aspect-Oriented Programming (AOP) to intercept method calls within specific packages and logs
 * the details such as class name, method name, and arguments.
 * <p>
 * The logging aspect targets methods in the `com.mb.laos.api.controller` and `com.mb.laos.service.impl` packages.
 * It ensures better traceability and debugging by providing insights into method execution details.
 *
 * @author LinhLH
 * @since October 2023
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
//    @Pointcut(AnnotationConstants.Pointcut.CONTROLLER + " || " + AnnotationConstants.Pointcut.SERVICE)
//    public void logBeforeFunctionPointcut() {
//
//    }
//
//    @Before("logBeforeFunctionPointcut()")
//    public void logBeforeFunctionAdvice(JoinPoint joinPoint) {
//        _log.info("Class {}. Function {}() with argument[s] = {}", joinPoint.getTarget().getClass().getSimpleName(),
//                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
//    }

}
