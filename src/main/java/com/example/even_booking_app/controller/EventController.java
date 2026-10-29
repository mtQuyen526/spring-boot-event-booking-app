package com.example.even_booking_app.controller;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.dto.response.*;
import com.example.even_booking_app.entity.Event;
import com.example.even_booking_app.exception.RequestValidationException;
import com.example.even_booking_app.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    // Create event
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request) {
        EventResponse eventResponse = eventService.createEvent(request);
        return new ApiResponse<>(
                true,
                "Create event successful",
                eventResponse
        );
    }

    //update event by id
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        EventResponse eventResponse = eventService.updateEvent(id, request);
        return new ApiResponse<>(
                true,
                "Event updated successfully",
                eventResponse
        );
    }

    // delete event by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    // get event
    @GetMapping
    public ApiResponse<List<EventResponse>> getEvents(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            HttpServletRequest request
    ) {



        // 5. Chỉ gọi Service khi tất cả tham số hợp lệ
        List<EventResponse> listEventResponse = eventService.getEvents( type, search,page, size, request );

        return new ApiResponse<>(
                true,
                "Events " + type.trim().toLowerCase(Locale.ROOT) + " fetched successfully",
                listEventResponse
        );
    }

    // get top event
    // popular / upcoming / nearby


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }


}
