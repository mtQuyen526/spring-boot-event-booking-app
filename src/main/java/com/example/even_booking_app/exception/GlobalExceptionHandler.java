package com.example.even_booking_app.exception;

import com.example.even_booking_app.dto.response.ApiError;
import com.example.even_booking_app.dto.response.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.Instant;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler { // nơi xử lý lỗi

    // 1. Lỗi validation từ @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ValidationError> errors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> new ValidationError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        ))
                        .toList();

        ApiError response = new ApiError(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                request.getRequestURI(),
                false,
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    // 2. Lỗi nghiệp vụ do ứng dụng chủ động ném ra
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        HttpStatus status = ex.getStatus();

        ApiError response = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                false,
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity.status(status).body(response);
    }

    // xử lý các validation
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiError> handleRequestValidationException(
            RequestValidationException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        ApiError apiError = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                false,
                ex.getMessage(),
                ex.getErrors()
        );

        return ResponseEntity.status(status).body(apiError);
    }


















    /*// 3. Lỗi không dự kiến
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError response = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                false,
                "An unexpected error occurred",
                List.of()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }*/
}


