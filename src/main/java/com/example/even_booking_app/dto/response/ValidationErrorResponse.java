package com.example.even_booking_app.dto.response;

public class ValidationErrorResponse<Error> {
    private String field;
    private String message;
    private Error error;
}
