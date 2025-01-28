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
 * Custom validator for date strings.
 * This validator checks if a given string value represents a valid date in the format "yyyy-MM-dd".
 */
public class DateValidator implements ConstraintValidator<DateValidation, String> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private DateTimeFormatter formatter;

    /**
     * Initializes the validator with the specified date format.
     *
     * @param constraintAnnotation The annotation instance for this validator.
     */
    @Override
    public void initialize(DateValidation constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.getDefault());
    }

    /**
     * Validates if the given string value represents a valid date.
     *
     * @param date    The string value to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the value represents a valid date, false otherwise.
     */
    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        return isValidDateFormat(date);
    }

    /**
     * Checks if the value is a valid date, date-time, or time format.
     *
     * @param value The string value to be checked.
     * @return true if the value is in a valid format, false otherwise.
     */
    private boolean isValidDateFormat(String value) {
        return isValidLocalDateTime(value) || isValidLocalDate(value) || isValidLocalTime(value);
    }

    /**
     * Attempts to parse the value as a LocalDateTime.
     *
     * @param value The string value to be parsed.
     * @return true if the value can be parsed as a LocalDateTime, false otherwise.
     */
    private boolean isValidLocalDateTime(String value) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(value, formatter);
            // Ensure the parsed and formatted values match to avoid false positives
            return value.equals(ldt.format(formatter));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Attempts to parse the value as a LocalDate.
     *
     * @param value The string value to be parsed.
     * @return true if the value can be parsed as a LocalDate, false otherwise.
     */
    private boolean isValidLocalDate(String value) {
        try {
            LocalDate ld = LocalDate.parse(value, formatter);
            // Ensure the parsed and formatted values match to avoid false positives
            return value.equals(ld.format(formatter));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Attempts to parse the value as a LocalTime.
     *
     * @param value The string value to be parsed.
     * @return true if the value can be parsed as a LocalTime, false otherwise.
     */
    private boolean isValidLocalTime(String value) {
        try {
            LocalTime lt = LocalTime.parse(value, formatter);
            // Ensure the parsed and formatted values match to avoid false positives
            return value.equals(lt.format(formatter));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}