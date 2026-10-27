package com.example.even_booking_app.dto.request;

import com.example.even_booking_app.validation.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class EventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Date & Time is required")
    @ValidDateTime
    private String dateTime;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    private BigDecimal price;

    @Size(
            max = 1000,
            message = "Description must be less than 1000 characters"
    )
    private String description;

    @URL(
            regexp = "^(https?://).*",
            message = "Invalid image URL"
    )
    private String imageUrl;

}
