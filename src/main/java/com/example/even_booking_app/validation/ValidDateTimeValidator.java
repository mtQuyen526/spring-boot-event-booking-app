package com.example.even_booking_app.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class ValidDateTimeValidator
        implements ConstraintValidator<ValidDateTime, String> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")
                    .withResolverStyle(ResolverStyle.STRICT);

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context) {

        // Để @NotBlank xử lý giá trị null hoặc rỗng.
        if (value == null || value.isBlank()) {
            return true;
        }

        LocalDateTime dateTime;

        try {
            dateTime = LocalDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException ex) {
            return addViolation(
                    context,
                    "Date & Time must be a valid date in format yyyy-MM-dd'T'HH:mm:ss"
            );
        }

        if (!dateTime.isAfter(LocalDateTime.now())) {
            return addViolation(
                    context,
                    "Date & Time must be in the future"
            );
        }

        return true;
    }

    private boolean addViolation(
            ConstraintValidatorContext context,
            String message) {

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();

        return false;
    }
}
