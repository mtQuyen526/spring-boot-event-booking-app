package com.example.even_booking_app.exception;

import org.springframework.http.HttpStatus;
/*Class này dùng để tạo một lớp ngoại lệ nghiệp vụ cha,
giúp các exception con tự quy định HTTP status tương ứng.*/
public abstract class BusinessException extends RuntimeException {  // exception tổng quát

    public BusinessException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();

}