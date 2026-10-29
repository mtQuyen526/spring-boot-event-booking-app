package com.example.even_booking_app.exception;

import com.example.even_booking_app.dto.response.ValidationError;
import org.springframework.http.HttpStatus;

import java.util.List;

public class RequestValidationException extends BusinessException {

    private final List<ValidationError> errors;

    public RequestValidationException(List<ValidationError> errors) {
        super("Validation failed");
        this.errors = List.copyOf(errors);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY; // 422
    }
}
