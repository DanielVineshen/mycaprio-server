package org.everowl.shared.service.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // let @NotNull handle null check if needed
        }

        try {
            LocalDate birthDate = LocalDate.parse(value,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
            LocalDate today = LocalDate.now(malaysiaZone);

            // Not in future
            if (birthDate.isAfter(today)) {
                return false;
            }

            // Not too old
            LocalDate maxAge = today.minusYears(120);
            return !birthDate.isBefore(maxAge);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}