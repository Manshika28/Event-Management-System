package com.example.userservice.repository;

import com.example.userservice.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ RowMapper for Booking
    private final RowMapper<Booking> bookingMapper = (rs, rowNum) -> {
        Booking b = new Booking();
        b.setId(rs.getLong("id"));
        b.setUserId(rs.getLong("user_id"));
        b.setEventId(rs.getLong("event_id"));
        b.setStatus(rs.getString("status"));
        return b;
    };

    // ✅ Save Booking
    public void save(Booking booking) {
        jdbcTemplate.update(
                "INSERT INTO bookings(user_id, event_id, status) VALUES (?, ?, ?)",
                booking.getUserId(), booking.getEventId(), booking.getStatus()
        );
    }

    // ✅ Find all bookings for a user
    public List<Booking> findByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM bookings WHERE user_id = ?", bookingMapper, userId);
    }

    // ✅ Find booking by ID
    public Booking findById(Long bookingId) {
        List<Booking> result = jdbcTemplate.query("SELECT * FROM bookings WHERE id = ?", bookingMapper, bookingId);
        return result.isEmpty() ? null : result.get(0);
    }

    // ✅ Delete booking
    public void delete(Long bookingId) {
        jdbcTemplate.update("DELETE FROM bookings WHERE id = ?", bookingId);
    }
}
