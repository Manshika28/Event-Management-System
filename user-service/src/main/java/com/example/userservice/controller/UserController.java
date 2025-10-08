package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.model.Booking;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;

    public UserController(UserRepository userRepo, BookingRepository bookingRepo) {
        this.userRepo = userRepo;
        this.bookingRepo = bookingRepo;
    }

    // ✅ Register
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User u) {
        userRepo.create(u);
        return ResponseEntity.ok(u);
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        User found = userRepo.findByEmail(u.getEmail());
        if (found != null && found.getPassword().equals(u.getPassword())) {
            return ResponseEntity.ok(found);
        }
        return ResponseEntity.status(401).body("unauthorized");
    }

    // ✅ Book Event
    @PostMapping("/{userId}/book/{eventId}")
    public ResponseEntity<Booking> bookEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setEventId(eventId);
        booking.setStatus("BOOKED");
        bookingRepo.save(booking);
        return ResponseEntity.ok(booking);
    }

    // ✅ View Bookings
    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingRepo.findByUserId(userId));
    }

    // ✅ Cancel Booking
    @DeleteMapping("/{userId}/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long userId, @PathVariable Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId);
        if (booking != null && booking.getUserId().equals(userId)) {
            bookingRepo.delete(bookingId);
            return ResponseEntity.ok("Booking cancelled");
        }
        return ResponseEntity.status(404).body("Booking not found");
    }

    // ✅ Update Profile
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody User updated) {
        User existing = userRepo.findById(userId);
        if (existing != null) {
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPassword(updated.getPassword());
            userRepo.save(existing);
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.status(404).body("User not found");
    }
}
