package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator for boolean values represented as strings.
 * This validator checks if a given string value is a valid boolean representation.
 */
public class BooleanValidator implements ConstraintValidator<BooleanValidation, String> {

    /**
     * Validates if the given string value represents a valid boolean.
     *
     * @param value   The string value to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the value is null or represents a valid boolean ("true" or "false", case-insensitive), false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        // Check if the value equals "true" or "false", ignoring case
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
}