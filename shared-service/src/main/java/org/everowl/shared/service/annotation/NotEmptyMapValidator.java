package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

/**
 * Custom validator to ensure that a Map is non-null and contains at least one entry.
 * This validator is used with the @NotEmptyMap constraint annotation.
 */
public class NotEmptyMapValidator implements ConstraintValidator<NotEmptyMap, Map<?, ?>> {

    /**
     * Validates that the given map is non-null and not empty.
     *
     * @param map     The map to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the map is non-null and contains at least one entry, false otherwise.
     */
    @Override
    public boolean isValid(Map<?, ?> map, ConstraintValidatorContext context) {
        return map != null && !map.isEmpty();
    }
}