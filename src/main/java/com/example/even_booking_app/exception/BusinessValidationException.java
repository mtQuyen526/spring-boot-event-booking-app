package com.example.even_booking_app.exception;


import org.springframework.http.HttpStatus;

/*Dùng cho các lỗi nghiệp vụ hoặc yêu cầu không hợp lệ mà bạn muốn trả về 422 Bad Request.*/
public class BusinessValidationException extends BusinessException {

    public BusinessValidationException(String message) {
        super(message);
    }
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY; // 422
    }
}
