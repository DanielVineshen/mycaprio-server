package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

/**
 * Custom validator to ensure that all keys in a Map are non-null and non-empty strings.
 * This validator is used with the @NotEmptyKeys constraint annotation.
 */
public class NotEmptyKeysValidator implements ConstraintValidator<NotEmptyKeys, Map<String, ?>> {

    /**
     * Validates that all keys in the given map are non-null and non-empty strings.
     *
     * @param map     The map to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the map is null or all keys are non-null and non-empty, false otherwise.
     */
    @Override
    public boolean isValid(Map<String, ?> map, ConstraintValidatorContext context) {
        if (map == null) {
            return true; // Null maps are considered valid
        }

        // Check each key in the map
        for (String key : map.keySet()) {
            if (key == null || key.trim().isEmpty()) {
                return false; // Return false if any key is null or empty (after trimming)
            }
        }

        return true; // All keys are non-null and non-empty
    }
}