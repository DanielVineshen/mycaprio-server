package org.everowl.shared.service.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation for validating optional date strings.
 * This annotation can be applied to fields or parameters to ensure they contain
 * either null or a valid date value in the format "yyyy-MM-dd".
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OptionalDateValidator.class)
public @interface OptionalDateValidation {
    /**
     * Specifies the error message to be used when the date validation fails.
     * This message is displayed when the field is not null and doesn't match the required format.
     *
     * @return The error message string.
     */
    String message() default "Date must be a valid format of yyyy-MM-dd format";

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