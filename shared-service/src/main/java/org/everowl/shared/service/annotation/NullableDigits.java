package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to validate that a numeric value has a specified maximum number of integer and fractional digits.
 * This annotation allows the validated field to be null.
 * It can be applied to fields, methods, parameters, or other annotations representing numeric values.
 */
@Constraint(validatedBy = NullableDigitsValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullableDigits {
    /**
     * Specifies the error message to be used when the validation fails.
     * The message can include placeholders {integer} and {fraction} which will be replaced
     * with the actual values specified in the annotation.
     *
     * @return The error message string.
     */
    String message() default "must be a number with at most {integer} integer digits and {fraction} fractional digits";

    /**
     * Specifies the validation groups to which this constraint belongs.
     *
     * @return An array of group classes.
     */
    Class<?>[] groups() default {};

    /**
     * Associates custom payload objects with the constraint.
     *
     * @return An array of payload classes.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies the maximum number of integer digits allowed.
     *
     * @return The maximum number of integer digits.
     */
    int integer();

    /**
     * Specifies the maximum number of fractional digits allowed.
     *
     * @return The maximum number of fractional digits.
     */
    int fraction();
}