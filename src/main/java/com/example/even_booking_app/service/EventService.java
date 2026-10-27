package com.example.even_booking_app.service;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.dto.response.EventResponse;
import com.example.even_booking_app.entity.Event;

import java.util.List;

public interface EventService {
    public EventResponse createEvent(EventRequest request);
    public List<Event> getAllEvents();
    public Event getEventById(Long id);
    public Event updateEvent(Long id, EventRequest request);
    public void deleteEvent(Long id);
}
