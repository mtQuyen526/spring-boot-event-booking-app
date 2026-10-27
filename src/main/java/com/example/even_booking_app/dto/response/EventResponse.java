package com.example.even_booking_app.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EventResponse {

    private Long id;

    private String title;

    private LocalDateTime dateTime;

    private String location;

    private BigDecimal price;

    private String description;

    private String imageUrl;
}
