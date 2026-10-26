package com.example.even_booking_app.exception;

import org.springframework.http.HttpStatus;

/*Dùng khi không tìm thấy tài nguyên.*/
public class ResourceNotFoundException extends BusinessException{
    public ResourceNotFoundException(String message){
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND; // 404
    }
}
