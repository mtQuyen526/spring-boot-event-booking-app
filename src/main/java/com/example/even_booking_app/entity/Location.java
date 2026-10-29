package com.example.even_booking_app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "location", nullable = false, length = 255)
    private String location;
    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
}
