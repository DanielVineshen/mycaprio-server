package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator to ensure that an array of strings contains no null elements.
 * This validator is used with the @NoNullElements constraint annotation.
 */
public class NoNullElementsValidator implements ConstraintValidator<NoNullElements, String[]> {

    /**
     * Validates that the given array of strings contains no null elements.
     *
     * @param strings                    The array of strings to be validated.
     * @param constraintValidatorContext The context in which the constraint is evaluated.
     * @return true if the array is null or contains no null elements, false otherwise.
     */
    @Override
    public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
        if (strings == null) {
            return true; // Null array is considered valid
        }

        // Check each element of the array for null
        for (String s : strings) {
            if (s == null) {
                return false; // Return false immediately if a null element is found
            }
        }

        return true; // All elements are non-null
    }
}