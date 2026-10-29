package com.example.even_booking_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình quyền truy cập API
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        //  POST event
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/events"
                        ).permitAll()
                        //  PUT event
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/events/{id}"
                        ).permitAll()
                        //  DELETE event
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/events/{id}"
                        ).permitAll()

                        // GET event

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/events"
                        ).permitAll()
                        .anyRequest().authenticated()

                );
        return http.build();
    }
}
