package com.example.even_booking_app.service.impl;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.entity.Event;
import com.example.even_booking_app.repository.EventRepository;
import com.example.even_booking_app.service.EventService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create event
    @Transactional
    @Override
    public Event createEvent(EventRequest request) {
        Event event = new Event();


        event.setTitle(request.getTitle());
        event.setDateTime(request.getDateTime());
        event.setLocation(request.getLocation());
        event.setPrice(request.getPrice());
        event.setDescription(request.getDescription());
        event.setImageUrl(request.getImageUrl());

        return eventRepository.save(event);
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
        event.setDateTime(request.getDateTime());
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

