package org.mbg.common.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.context.support.StandardServletEnvironment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LinhLH
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtil {

    private static final String[] FIELD_NAME_SKIP =
                    {"serialVersionUID", "pageIndex", "pageSize", "orderByColumn", "orderByType", "length"};

    public static boolean hasProperty(Class<?> c, String name) {
        List<Field> fields = FieldUtils.getAllFieldsList(c);

        return fields.stream().anyMatch(f -> Validator.equals(f.getName(), name));
    }

    public static Object getFieldValue(Object ob, String name) {
        try {
            Field field = ob.getClass().getDeclaredField(name);

            field.setAccessible(true);

            return field.get(ob);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            _log.error("Cannot get field value {} ", name);
        }

        return null;
    }

    public static <T extends Serializable> T clone(final T entity) {
        return SerializationUtils.clone(entity);
    }

    /**
     * check object has all attribute is null
     *
     * @param object the object to check
     * @return true if object is null
     */
    public static boolean isNullInputObject(Object object) {
        Class<?> clazz = object.getClass();

        try {
            List<Field> allFields = FieldUtils.getAllFieldsList(clazz);

            for (Field field : allFields) {
                // set accessible by file private to get value
                field.setAccessible(true);

                // skip if type is static final
                String fieldName = field.getName();

                if (Arrays.asList(FIELD_NAME_SKIP).contains(fieldName)) {
                    continue;
                }

                Object valueOfAttribute = field.get(object);

                if (valueOfAttribute != null) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            _log.error("Error get field: ", e);
        }

        return true;
    }

    public static Class<?>[] findAllClassesWithAnnotation(String packageName,
                    Class<? extends Annotation> annotationClass) {
        final List<Class<?>> result = new LinkedList<>();

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                        true, new StandardServletEnvironment());

        provider.addIncludeFilter(new AnnotationTypeFilter(annotationClass));

        for (BeanDefinition beanDefinition : provider
                        .findCandidateComponents(packageName)) {
            try {
                result.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                _log.warn("Could not resolve class object for bean definition", e);
            }
        }

        return result.toArray(new Class<?>[0]);
    }

}
