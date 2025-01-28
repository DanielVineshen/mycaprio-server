package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Custom validator class for optional date validation.
 * Implements ConstraintValidator interface to validate optional date strings
 * against a specified format (yyyy-MM-dd).
 * This validator allows null values and considers them valid.
 */
public class OptionalDateValidator implements ConstraintValidator<OptionalDateValidation, String> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private DateTimeFormatter formatter;

    /**
     * Initializes the validator with the specified date format.
     *
     * @param constraintAnnotation The OptionalDateValidation annotation instance
     */
    @Override
    public void initialize(OptionalDateValidation constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.getDefault());
    }

    /**
     * Validates the given date string.
     *
     * @param date    The date string to validate
     * @param context The constraint validator context
     * @return true if the date is null or valid according to the specified format, false otherwise
     */
    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Null values are considered valid
        }
        return isValidDateFormat(date);
    }

    /**
     * Checks if the given value is a valid date format by attempting to parse it
     * as LocalDateTime, LocalDate, or LocalTime.
     *
     * @param value The string to validate
     * @return true if the value is a valid date format, false otherwise
     */
    private boolean isValidDateFormat(String value) {
        return isValidLocalDateTime(value) || isValidLocalDate(value) || isValidLocalTime(value);
    }

    /**
     * Attempts to parse the value as a LocalDateTime.
     *
     * @param value The string to parse
     * @return true if the value is a valid LocalDateTime and matches the specified format, false otherwise
     */
    private boolean isValidLocalDateTime(String value) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(value, formatter);
            return value.equals(ldt.format(formatter)); // Ensure the parsed and formatted values match
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Attempts to parse the value as a LocalDate.
     *
     * @param value The string to parse
     * @return true if the value is a valid LocalDate and matches the specified format, false otherwise
     */
    private boolean isValidLocalDate(String value) {
        try {
            LocalDate ld = LocalDate.parse(value, formatter);
            return value.equals(ld.format(formatter)); // Ensure the parsed and formatted values match
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Attempts to parse the value as a LocalTime.
     *
     * @param value The string to parse
     * @return true if the value is a valid LocalTime and matches the specified format, false otherwise
     */
    private boolean isValidLocalTime(String value) {
        try {
            LocalTime lt = LocalTime.parse(value, formatter);
            return value.equals(lt.format(formatter)); // Ensure the parsed and formatted values match
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
