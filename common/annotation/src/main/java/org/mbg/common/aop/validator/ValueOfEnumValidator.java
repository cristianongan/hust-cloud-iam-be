package org.mbg.common.aop.validator;

import org.mbg.common.annotation.EnumType;
import org.mbg.common.annotation.ValueOfEnum;
import org.mbg.common.util.Validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * A custom validator that checks if a given value is one of the allowed enumeration values defined by the {@link ValueOfEnum} annotation.
 * This validator supports enums of two types: STRING_NAME (enum names) and KEY_VALUE (custom values defined within the enumeration).
 * <p>
 * For enums with the KEY_VALUE type, the enumeration is required to provide a method (specified in {@link ValueOfEnum#methodOfValues()})
 * that returns the list of valid values.
 * <p>
 * Implements the {@link ConstraintValidator} interface, making it compatible with javax.validation’s annotation-based validation.
 * Logs warnings when validation fails or when the required methods for KEY_VALUE enums are not properly defined.
 * <p>
 *
 * <b>Usage:</b><br>
 * Annotate fields or methods with the {@link ValueOfEnum} annotation to enable validation with this validator.
 * <p>
 *
 * Example:
 * <pre>
 * &#64;ValueOfEnum(enumClass = MyEnum.class, enumType = EnumType.STRING_NAME)
 * private String myEnumValue;
 * </pre>
 * <p>
 *
 * Example with KEY_VALUE:
 * <pre>
 * &#64;ValueOfEnum(enumClass = MyEnum.class, enumType = EnumType.KEY_VALUE, methodOfValues = "getValues")
 * private String myEnumValue;
 * </pre>
 *
 * @see ValueOfEnum
 * @see ConstraintValidator
 * @see EnumType
 *
 * @author LinhLH
 * @since October 10, 2023
 */
@Slf4j
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {

    /**
     * Represents a list of values. This is initialized as an empty list
     * and can store a collection of objects.
     * <p>
     * This field is immutable after initialization.
     *
     * @since October 9, 2023
     */
    private final List<Object> values = new ArrayList<>();

    /**
     * This class is responsible for initializing the constraints for an enum type.
     * It processes the provided annotation and sets up the corresponding enum values based on
     * the provided type.
     *
     * <p>
     * This implementation handles both {@code EnumType.STRING_NAME} and other enum types.
     * For {@code EnumType.STRING_NAME}, it extracts all names of the enum constants;
     * otherwise, it invokes a helper method to process the enum values.
     *
     * <p>
     * Note: The helper method {@code getEnumValue} is private/internal to manage values for non-string-name types.
     */
    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        if (constraintAnnotation.enumType().name().equals(EnumType.STRING_NAME.name())) {
            values.addAll(Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                    .map(Enum::name)
                    .toList());
        } else {
            this.getEnumValue(constraintAnnotation);
        }

    }

    /**
     * Validates whether the given value is valid based on the specified constraints.
     * <p>
     * This method checks if the provided value matches the expected criteria, depending
     * on its type. If the value is a collection, the method ensures that it contains any
     * of the expected values. If it's a single value, it checks if it exists within the
     * predefined list of valid values. Additionally, null or empty values are handled
     * and considered valid in specific cases.
     *
     * @param value the object to be validated. This can be a String, a Collection, or another object.
     * @param context contextual data and operation when applying the validator. It provides
     *                additional metadata or configuration options for validation.
     * @return {@code true} if the value is valid based on the constraints; {@code false} otherwise.
     *
     * @author LinhLH
     * @since October 18, 2023
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Validator.isNotNull(value) && value instanceof String) {
            value = ((String) value).toUpperCase();
        }

        if (value instanceof Collection) {
            return CollectionUtils.containsAny(values, (Collection<?>) value);
        } else {
            return Validator.isNull(value) || values.contains(value);
        }

    }

    /**
     * This method retrieves and processes the enumeration values using the provided annotation.
     * It uses reflection to invoke a specified method on the enum class to fetch its values.
     *
     * <p>
     * This method also logs warnings if the required method is not found or an error occurs during its execution.
     *
     * @author LinhLH
     * @since October 16, 2023
     *
     * @param constraintAnnotation The annotation containing the information about the enum class
     *                              and the method to be invoked for retrieving enum values.
     */
    private void getEnumValue(ValueOfEnum constraintAnnotation) {
        Class<?> enumClazz = constraintAnnotation.enumClass();
        
        Object[] enumConstants = enumClazz.getEnumConstants();

        if (enumConstants == null) {
            return;
        }
        
        Method[] methods = enumClazz.getDeclaredMethods();

        Method method = Stream.of(methods)
                        .filter(methodElement -> methodElement.getName().equals(constraintAnnotation.methodOfValues()))
                        .findFirst().orElse(null);

        if (method == null) {
            _log.warn(" enumerable object :[{}] There is no method in :[{}], Please check .", enumClazz.getName(),
                            constraintAnnotation.methodOfValues());
            return;
        }

        method.setAccessible(true);
        
        try {
            // excuse method get values of enum
            Object valueList = method.invoke(this);
            if (valueList instanceof Collection) {
                values.addAll((Collection<?>) valueList);
            } else {
                values.add(valueList);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            _log.warn(" Get enumeration class :[{}] Failed to enumerate the value of the object .", enumClazz);
        }
    }
}

