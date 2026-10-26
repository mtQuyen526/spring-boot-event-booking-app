package com.example.even_booking_app.service;

import com.example.even_booking_app.dto.request.LoginRequest;
import com.example.even_booking_app.dto.request.RegisterRequest;
import com.example.even_booking_app.dto.response.LoginResponse;
import com.example.even_booking_app.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
