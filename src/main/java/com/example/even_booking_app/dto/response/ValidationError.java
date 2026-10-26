package com.example.even_booking_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError { //    trả về một lỗi

    private String field;
    private String message;
}
