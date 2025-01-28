package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator to ensure that a numeric value (String, Float, or Double) has a specified number of integer and fraction digits.
 * This validator allows null values and is used with the @NullableDigits constraint annotation.
 */
public class NullableDigitsValidator implements ConstraintValidator<NullableDigits, Object> {

    private int integer;
    private int fraction;

    /**
     * Initializes the validator with the specified integer and fraction digit limits.
     *
     * @param constraintAnnotation The NullableDigits annotation instance.
     */
    @Override
    public void initialize(NullableDigits constraintAnnotation) {
        this.integer = constraintAnnotation.integer();
        this.fraction = constraintAnnotation.fraction();
    }

    /**
     * Validates that the given value adheres to the specified integer and fraction digit limits.
     *
     * @param value   The object to be validated (should be String, Float, or Double).
     * @param context The context in which the constraint is evaluated.
     * @return true if the value is null or adheres to the digit limits, false otherwise.
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        String stringValue;
        if (value instanceof Float || value instanceof Double) {
            stringValue = value.toString();
        } else if (value instanceof String) {
            stringValue = (String) value;
        } else {
            return false; // Unsupported type
        }

        // Check if the stringValue is a valid number
        if (!isNumeric(stringValue)) {
            return false;
        }

        // Split the number into integer and fraction parts
        String[] parts = stringValue.split("\\.");
        String integerPart = parts[0];
        String fractionPart = parts.length > 1 ? parts[1] : "";

        // Check if the integer and fraction parts adhere to the specified limits
        return integerPart.length() <= integer && fractionPart.length() <= fraction;
    }

    /**
     * Checks if a string represents a valid numeric value.
     *
     * @param str The string to be checked.
     * @return true if the string is a valid numeric value, false otherwise.
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}