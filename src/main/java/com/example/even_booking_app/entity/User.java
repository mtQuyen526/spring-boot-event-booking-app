package com.example.even_booking_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 512)
    private String avatar;

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String roles = Role.USER.toString();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (roles == null || roles.isBlank()) {
            roles = Role.USER.toString();
        }
    }

    /*public void setRoles(String roles) {

        Set<String> listRoles = roles == null
                || roles.isBlank()
                ? new HashSet<>()
                : Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .collect(Collectors.toSet());
        roles = String.join(",", listRoles);

        if (roles.equals(Role.ADMIN.toString())) // tránh trường hợp người dùng tự ý đaăng kí quyền ADMIN
            this.roles = roles;
        else
            this.roles = Role.USER.toString();
    }*/
}

