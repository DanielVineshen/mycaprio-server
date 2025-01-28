package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation to validate that a Map is not empty.
 * This annotation can be applied to methods, fields, or other annotations
 * that represent Map objects.
 */
@Documented
@Constraint(validatedBy = NotEmptyMapValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyMap {
    /**
     * Specifies the error message to be used when the validation fails.
     *
     * @return The error message string.
     */
    String message() default "Map cannot be empty";

    /**
     * Specifies the validation groups to which this constraint belongs.
     * This allows for the validation to be applied only to certain groups of constraints.
     *
     * @return An array of group classes.
     */
    Class<?>[] groups() default {};

    /**
     * Associates custom payload objects with the constraint.
     * This can be used to provide additional metadata for the validation process.
     *
     * @return An array of payload classes.
     */
    Class<? extends Payload>[] payload() default {};
}