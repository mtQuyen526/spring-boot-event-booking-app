package com.example.even_booking_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/*Chọn một class response lỗi thống nhất*/
@Getter
@AllArgsConstructor
public class ApiError {
    private Instant timestamp;  //  thời gian lỗi
    private int status;  // mã lỗi
    private String error; // loại lỗi
    private String path; // Api lỗi
    private Boolean success; // trạng thái
    private String message; // thông báo lỗi
    private List<ValidationError> errors; // trường bị lỗi cụ thể
}
