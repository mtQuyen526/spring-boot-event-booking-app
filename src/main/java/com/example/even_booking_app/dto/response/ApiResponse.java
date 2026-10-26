package com.example.even_booking_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiResponse<T> { // Class dùng để chuẩn hóa cấu trúc JSON trả về khi gọi API
    /*-------------------------------------------------------------------*/
    private boolean success; // trường Boolean dùng để thông báo thao tác thành công hay thất bại.
    private String message;
    private T data;
}
