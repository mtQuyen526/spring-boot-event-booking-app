package com.example.even_booking_app.service.impl;

import com.example.even_booking_app.dto.request.LoginRequest;
import com.example.even_booking_app.dto.response.LoginResponse;
import com.example.even_booking_app.entity.Role;
import com.example.even_booking_app.entity.User;
import com.example.even_booking_app.exception.BusinessConflictException;
import com.example.even_booking_app.exception.InvalidCredentialsException;
import com.example.even_booking_app.repository.UserRepository;
import com.example.even_booking_app.service.AuthService;
import com.example.even_booking_app.dto.response.RegisterResponse;
import com.example.even_booking_app.dto.request.RegisterRequest;
import com.example.even_booking_app.exception.BusinessValidationException;
import com.example.even_booking_app.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.Instant;
import java.util.Date;
import java.util.Locale;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public RegisterResponse register(RegisterRequest request) {

        // 1. Kiểm tra email đã tồn tại
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        // Phát hiện lỗi nghiệp vụ và ném exception
        if (userRepository.existsByEmail(email)) {
            throw new BusinessConflictException(
                    "Email is already registered"
            );
        }

        // 2. Tạo User mới
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(email);


        // 3. Mã hóa password trước khi lưu
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // 4. Lưu User vào database
        User savedUser = userRepository.save(user);

        // 5. Trả về response DTO
        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(email);

        return response;
    } // xong

    @Override
    public LoginResponse login(LoginRequest request) {

        // 1. Tìm tài khoản theo email
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email"
                        )
                );

        // 2. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Password is incorrect"
            );
        }

        // 3. Tạo JWT và xác định thời điểm hết hạn


        String accessToken = jwtService.generateAccessToken(user);
        Instant  expire = jwtService.getExpiration(accessToken);

        // 4. Trả về thông tin đăng nhập
        return new LoginResponse(
                accessToken,
                expire
        );


    } // xong
}

