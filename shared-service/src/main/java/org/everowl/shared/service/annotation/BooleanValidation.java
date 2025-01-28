package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation for validating boolean values.
 * This annotation can be applied to fields or parameters to ensure they contain
 * valid boolean values (true or false).
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BooleanValidator.class)
public @interface BooleanValidation {
    /**
     * Defines the error message to be used when the validation fails.
     *
     * @return The error message string.
     */
    String message() default "Status value must only either be true or false";

    /**
     * Specifies the validation groups to which this constraint belongs.
     * This allows for validation to be applied only to certain groups of constraints.
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