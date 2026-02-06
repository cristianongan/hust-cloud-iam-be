package com.hust.common.security.aop;

import com.hust.common.security.annotation.Permission;
import com.hust.common.security.annotation.PermissionAny;
import com.hust.common.security.exception.NoPermissionException;
import com.hust.common.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Aspect for handling permission checks on methods annotated with specific permission annotations.
 * <p>
 * This aspect intercepts method calls to check if the current user has the required privileges to execute
 * methods annotated with permission annotations. It ensures that security constraints are enforced before
 * method execution, throwing a SecurityException if the user lacks the necessary privileges.
 * <p>
 * The aspect utilizes Spring AOP and is marked as a component for dependency injection.
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

    /**
     * Checks if the current user has the required privilege to execute a method annotated with a specific permission.
     * <p>
     * This method is executed before any method annotated with the specified permission annotation. It retrieves the
     * current user's authentication details and checks if the user has the necessary privilege to proceed. If the user
     * lacks the required privilege, a SecurityException is thrown.
     *
     * @param joinPoint the join point representing the method being intercepted
     * @param permission the permission annotation containing the required privilege information
     * @throws NoPermissionException if the user does not have the required privilege
     */
    @Before("@annotation(permission)")
    public void checkPrivilege(JoinPoint joinPoint, Permission permission) {
        _log.info("[checkPrivilege] Method has been call to check permission for {}",
                joinPoint.getSignature().getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String permissionCode = permission.value();
        String httpMethod = getHttpMethod(joinPoint);

        String requiredPrivilege = Optional.ofNullable(httpMethod)
                .map(method -> String.join(StringPool.COLON, permissionCode, method))
                .orElse(permissionCode);

        boolean hasAccess = hasAnyPermission(authentication, Collections.singletonList(requiredPrivilege));

        if (!hasAccess) {
            throw new NoPermissionException();
        }
    }

    /**
     * Checks if the current user has any of the specified permissions before executing the annotated method.
     * <p>
     * This method is triggered by the `@Before` aspect annotation and verifies if the user has at least one of the
     * required permissions specified in the `PermissionAny` annotation. It constructs the required privileges based
     * on the HTTP method and permission codes, and checks the user's authentication context for access rights.
     *
     * @param joinPoint the join point representing the method execution
     * @param permissionAny the annotation containing the permission codes to check against
     * @throws NoPermissionException if the user does not have any of the required permissions
     */
    @Before("@annotation(permissionAny)")
    public void checkPrivilegeAny(JoinPoint joinPoint, PermissionAny permissionAny) {
        _log.info("[checkPrivilegeAny] Method has been call to check permission for {}",
                joinPoint.getSignature().getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String[] permissionCodes = permissionAny.value();
        String httpMethod = getHttpMethod(joinPoint);

        List<String> requiredPrivileges = Optional.ofNullable(httpMethod)
                .map(method -> Arrays.stream(permissionCodes)
                        .map(p -> String.join(StringPool.COLON, p, method))
                        .toList())
                .orElse(Arrays.asList(permissionCodes));

        boolean hasAccess = hasAnyPermission(authentication, requiredPrivileges);

        if (!hasAccess) {
            throw new NoPermissionException();
        }

    }


    /**
     * Checks if the given authentication has any of the specified permissions.
     * <p>
     * This method evaluates whether the provided authentication object possesses at least one of the
     * permissions listed in the provided collection. It does so by comparing the authorities granted
     * to the authentication with the specified permissions.
     *
     * @param authentication the authentication object containing granted authorities
     * @param permissions a list of permissions to check against the authentication's authorities
     * @return true if the authentication has at least one of the specified permissions, false otherwise
     */

    private boolean hasAnyPermission(Authentication authentication, List<String> permissions) {
        return authentication.getAuthorities().stream().anyMatch(grantedAuthority ->
                permissions.contains(grantedAuthority.getAuthority()));
    }

    /**
     * Determines the HTTP method type of a given method based on its annotations.
     * <p>
     * This method inspects the annotations present on the method associated with the provided
     * join point to identify the HTTP method type (GET, POST, PUT, DELETE, PATCH).
     *
     * @param joinPoint the join point representing the method execution
     * @return the name of the HTTP method as a string, or null if no relevant annotation is found
     */
    private String getHttpMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (method.isAnnotationPresent(GetMapping.class)) {
            return HttpMethod.GET.name();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return HttpMethod.POST.name();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return HttpMethod.PUT.name();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return HttpMethod.DELETE.name();
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return HttpMethod.PATCH.name();
        }

        return null;
    }
}
