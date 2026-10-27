package com.example.even_booking_app.service.impl;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.dto.response.EventResponse;
import com.example.even_booking_app.entity.Event;
import com.example.even_booking_app.repository.EventRepository;
import com.example.even_booking_app.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;


    // Create event
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

    // Get all events
    @Transactional(readOnly = true)
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get event by ID
    @Transactional(readOnly = true)
    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + id));
    }

    // Update event
    @Transactional
    @Override
    public Event updateEvent(Long id, EventRequest request) {
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

        return eventRepository.save(event);
    }

    // Delete event
    @Transactional
    @Override
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }
}

