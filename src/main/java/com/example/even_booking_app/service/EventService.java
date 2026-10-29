package com.example.even_booking_app.service;

import com.example.even_booking_app.dto.request.EventRequest;
import com.example.even_booking_app.dto.response.EventResponse;
import com.example.even_booking_app.entity.Event;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface EventService {
    public EventResponse createEvent(EventRequest request);
    public EventResponse updateEvent(Long id, EventRequest request);
   // private Event getEventById(Long id);

    public List<EventResponse> getEvents(String type, String search, String page, String size, HttpServletRequest request);
    public Event getEventById(Long id);

    public void deleteEvent(Long id);
}
