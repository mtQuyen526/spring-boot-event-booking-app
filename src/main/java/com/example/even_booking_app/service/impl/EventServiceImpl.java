package com.example.even_booking_app.service.impl;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.dto.response.EventResponse;
import com.example.even_booking_app.dto.response.ValidationError;
import com.example.even_booking_app.entity.Event;
import com.example.even_booking_app.exception.RequestValidationException;
import com.example.even_booking_app.exception.ResourceNotFoundException;
import com.example.even_booking_app.repository.EventRepository;
import com.example.even_booking_app.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;


    // Create event
    @Transactional
    @Override
    public EventResponse createEvent(EventRequest request) {

        Event event = new Event();


        event.setTitle(request.getTitle());
        // xử lý chuỗi String trong request.getDateTime() thành LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(
                request.getDateTime(),
                DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")
                        .withResolverStyle(ResolverStyle.STRICT)
        );
        event.setDateTime(dateTime);
        event.setLocation(request.getLocation());
        event.setPrice(request.getPrice());
        event.setDescription(request.getDescription());
        event.setImageUrl(request.getImageUrl());
        event.setLatitude(new BigDecimal("00.00000000"));
        event.setLongitude(new BigDecimal("00.00000000"));

        eventRepository.save(event);
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation(),
                event.getPrice(),
                event.getDescription(),
                event.getImageUrl()
        );
    }

    // Update event
    @Transactional
    @Override
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = getEventById(id);

        event.setTitle(request.getTitle());
        // xử lý chuỗi String trong request.getDateTime() thành LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(
                request.getDateTime(),
                DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss")
                        .withResolverStyle(ResolverStyle.STRICT)
        );
        event.setDateTime(dateTime);
        event.setLocation(request.getLocation());
        event.setPrice(request.getPrice());
        event.setDescription(request.getDescription());
        event.setImageUrl(request.getImageUrl());

        eventRepository.save(event);
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDateTime(),
                event.getLocation(),
                event.getPrice(),
                event.getDescription(),
                event.getImageUrl()
        );
    }


    // Get events
    @Transactional(readOnly = true)
    @Override
    public List<EventResponse> getEvents(String type, String search, String page, String size, HttpServletRequest request) {

        if (!StringUtils.hasText(type)
                && !StringUtils.hasText(search)
                && !StringUtils.hasText(page)
                && !StringUtils.hasText(size)) {
            return getAllEvents();
            // Xử lý khi tất cả tham số không có nội dung
        }

        List<ValidationError> errors = new ArrayList<>();

        // 1. Kiểm tra type
        if (type == null || type.isBlank()) {
            errors.add(new ValidationError(
                    "type", "Type is required"
            ));
        } else if (!Set.of( "upcoming", "popular", "nearby")
                .contains(type.trim().toLowerCase(Locale.ROOT))) {
            errors.add(new ValidationError(
                    "type", "Type must be upcoming, popular or nearby"
            ));
        }

        // 2. Kiểm tra page
        int pageValue = 1;

        if (page != null) {
            try {
                pageValue = Integer.parseInt(page.trim());

                if (pageValue < 1) {
                    errors.add(new ValidationError(
                            "page", "Page must be greater than or equal to 1"
                    ));
                }
            } catch (NumberFormatException ex) {
                errors.add(new ValidationError(
                        "page", "Page must be a valid integer"
                ));
            }
        }

        // 3. Kiểm tra size
        int sizeValue = 10;

        if (size != null) {
            try {
                sizeValue = Integer.parseInt(size.trim());

                if (sizeValue < 1 || sizeValue > 100) {
                    errors.add(new ValidationError(
                            "size", "Size must be between 1 and 100"
                    ));
                }
            } catch (NumberFormatException ex) {
                errors.add(new ValidationError(
                        "size", "Size must be a valid integer"
                ));
            }
        }

        // 4. Nếu có lỗi, trả về tất cả lỗi cùng lúc
        if (!errors.isEmpty()) {
            throw new RequestValidationException(errors);
        }


        return switch (type.toLowerCase().trim()) {

            case "popular" -> getPopularEvents(sizeValue);

            case "upcoming" -> getUpComingEvents(sizeValue);

            case "nearby" -> getNearbyEvents(sizeValue, BigDecimal.ZERO, BigDecimal.ZERO, 50);

            default -> throw new IllegalArgumentException(
                    "Unsupported event type: " + type
            );
        };

    }


    /* viết 4 hàm get event
     * hàm 1, getall
     * ham 2, get popular
     * hàm 3 get upcoming
     * hàm 3, get nearby*/

    private List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(event -> {
                    EventResponse response = new EventResponse();

                    response.setId(event.getId());
                    response.setTitle(event.getTitle());
                    response.setDateTime(event.getDateTime());
                    response.setLocation(event.getLocation());
                    response.setPrice(event.getPrice());
                    response.setDescription(event.getDescription());
                    response.setImageUrl(event.getImageUrl());

                    return response;
                })
                .collect(Collectors.toList());
    }

    // Get popular events
    private List<EventResponse> getPopularEvents(int size) {
        return eventRepository.findTopEventsByBookingCount(size)
                .stream()
                .map(event -> {
                    EventResponse response = new EventResponse();

                    response.setId(((Number) event.get("id")).longValue());
                    response.setTitle((String) event.get("title"));
                    response.setDateTime(
                            convertDateTime(event.get("dateTime"))
                    );
                    response.setPrice((BigDecimal) event.get("price"));
                    response.setLocation((String) event.get("location"));
                    response.setDescription((String) event.get("description"));
                    response.setImageUrl((String) event.get("imageUrl"));

                    return response;
                })
                .collect(Collectors.toList());
    }

    // Get upcoming events
    private List<EventResponse> getUpComingEvents(int size) {
        return eventRepository.findUpcomingEvents(size)
                .stream()
                .map(event -> {
                    EventResponse response = new EventResponse();
                    response.setId(((Number) event.get("id")).longValue());
                    response.setTitle((String) event.get("title"));
                    response.setDateTime(
                            convertDateTime(event.get("dateTime"))
                    );
                    response.setPrice((BigDecimal) event.get("price"));
                    response.setLocation((String) event.get("location"));
                    response.setDescription((String) event.get("description"));
                    response.setImageUrl((String) event.get("imageUrl"));


                    return response;
                })
                .collect(Collectors.toList());
    }

    // Get nearby events
    private List<EventResponse> getNearbyEvents(int size, BigDecimal userLat, BigDecimal userLng, double radius) {
        return eventRepository.findNearbyEvents(userLat, userLng, radius, size)
                .stream()
                .map(event -> {
                    EventResponse response = new EventResponse();

                    response.setId(((Number) event.get("id")).longValue());
                    response.setTitle((String) event.get("title"));
                    response.setDateTime(
                            convertDateTime(event.get("dateTime"))
                    );
                    response.setPrice((BigDecimal) event.get("price"));
                    response.setLocation((String) event.get("location"));
                    response.setDescription((String) event.get("description"));
                    response.setImageUrl((String) event.get("imageUrl"));


                    return response;
                })
                .collect(Collectors.toList());
    }


    // Get event by ID
    @Transactional(readOnly = true)
    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found with id: " + id));
    }


    // Delete event
    @Transactional
    @Override
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }


    private LocalDateTime convertDateTime(Object value) {
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }

        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        throw new IllegalArgumentException(
                "Unsupported dateTime type: "
                        + (value == null ? "null" : value.getClass().getName())
        );
    }
}

