package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator to ensure that a String represents a valid integer.
 * This validator is used with the @ValidInteger constraint annotation.
 * It allows null and empty values, assuming that @NotBlank or similar constraints
 * will handle those cases if needed.
 */
public class ValidIntegerValidator implements ConstraintValidator<ValidInteger, String> {

    /**
     * Validates that the given string represents a valid integer.
     *
     * @param value   The string to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the value is null, empty, or a valid integer, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Null or empty values are considered valid
        }

        try {
            Integer.parseInt(value);
            return true; // Successfully parsed as an integer
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }
}
