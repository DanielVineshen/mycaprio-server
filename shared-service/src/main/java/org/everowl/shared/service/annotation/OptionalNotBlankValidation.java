package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation for validating optional string fields.
 * This annotation can be applied to fields or parameters to ensure that if a value is present,
 * it is not blank (i.e., not null, not an empty string, and not only whitespace).
 * If the field is null, it is considered valid.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OptionalNotBlankValidator.class)
public @interface OptionalNotBlankValidation {
    /**
     * Specifies the error message to be used when the validation fails.
     * This message is displayed when the field is present but blank.
     *
     * @return The error message string.
     */
    String message() default "This field must not be empty if present";

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
