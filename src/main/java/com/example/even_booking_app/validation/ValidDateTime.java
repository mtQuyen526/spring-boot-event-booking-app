package com.example.even_booking_app.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidDateTimeValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateTime {

    String message() default
            "Date & Time must follow yyyy-MM-dd'T'HH:mm:ss and be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}