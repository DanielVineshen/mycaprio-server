package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation to validate that an array or collection does not contain any null elements.
 * This annotation can be applied to fields or parameters of array or collection types.
 */
@Documented
@Constraint(validatedBy = NoNullElementsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNullElements {
    /**
     * Specifies the error message to be used when the validation fails.
     *
     * @return The error message string.
     */
    String message() default "Array cannot contain null elements";

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