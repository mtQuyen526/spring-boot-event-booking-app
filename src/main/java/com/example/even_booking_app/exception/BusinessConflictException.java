package com.example.even_booking_app.exception;

import org.springframework.http.HttpStatus;

/*Dùng khi dữ liệu xung đột, chẳng hạn email đã được đăng ký.*/
public class BusinessConflictException extends BusinessException {
    public BusinessConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT; // 409
    }
}