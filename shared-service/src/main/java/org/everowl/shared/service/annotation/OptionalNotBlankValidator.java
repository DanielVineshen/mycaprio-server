package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator class for optional non-blank string validation.
 * Implements ConstraintValidator interface to validate that a string is either null
 * or not blank (i.e., not empty or consisting only of whitespace).
 * This validator allows null values and considers them valid.
 */
public class OptionalNotBlankValidator implements ConstraintValidator<OptionalNotBlankValidation, String> {

    /**
     * Validates the given string value.
     *
     * @param value   The string value to validate
     * @param context The constraint validator context
     * @return true if the value is null or not blank (after trimming), false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null values are considered valid
        // For non-null values, trim and check if the result is not empty
        return value == null || !value.trim().isEmpty();
    }
}