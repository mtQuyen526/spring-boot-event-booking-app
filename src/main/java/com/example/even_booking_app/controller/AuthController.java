package com.example.even_booking_app.controller;

import com.example.even_booking_app.dto.request.LoginRequest;
import com.example.even_booking_app.dto.request.RegisterRequest;
import com.example.even_booking_app.dto.response.ApiResponse;
import com.example.even_booking_app.dto.response.LoginResponse;
import com.example.even_booking_app.dto.response.RegisterResponse;
import com.example.even_booking_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        RegisterResponse user = authService.register(request);

        return new ApiResponse<>(
                true,
                "Registration successful",
                user
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse data = authService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "Login successful",
                data
        );

        return ResponseEntity.ok(response);
    }

}
