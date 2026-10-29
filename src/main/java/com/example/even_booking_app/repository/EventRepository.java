package com.example.even_booking_app.repository;

import com.example.even_booking_app.dto.response.EventResponse;
import com.example.even_booking_app.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    @Query(value = """
            SELECT
                e.id AS id,
                e.title AS title,
                e.date_time AS dateTime,
                e.price AS price,
                COUNT(b.id) AS bookingCount
            FROM events e
            LEFT JOIN bookings b ON e.id = b.event_id
            GROUP BY e.id, e.title, e.date_time, e.location, e.price, e.description, e.imageUrl
            ORDER BY bookingCount DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Map<String, Object>> findTopEventsByBookingCount(@Param("limit") int limit);

    @Query(value = """
            SELECT *
            FROM events
            WHERE date_time >= NOW()
            ORDER BY date_time ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<Map<String, Object>> findUpcomingEvents(@Param("limit") int limit);

    @Query(value = """
            SELECT e.*,
                (6371 * ACOS(
                    COS(RADIANS(:user_lat)) *
                    COS(RADIANS(e.latitude)) *
                    COS(RADIANS(e.longitude) - RADIANS(:user_lng)) +
                    SIN(RADIANS(:user_lat)) *
                    SIN(RADIANS(e.latitude))
                )) AS distance
            FROM events e
            HAVING distance < :radius
            ORDER BY distance ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<Map<String, Object>> findNearbyEvents(
            @Param("user_lat") BigDecimal userLat,
            @Param("user_lng") BigDecimal userLng,
            @Param("radius") double radius, // khoan cách tối thiểu mong muốn
            @Param("limit") int limit
    );


}
