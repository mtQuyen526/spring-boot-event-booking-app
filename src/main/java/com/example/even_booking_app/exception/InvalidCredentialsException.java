package com.example.even_booking_app.exception;


import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {  // nơi xử lý lỗi đăng nhập

    public InvalidCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}