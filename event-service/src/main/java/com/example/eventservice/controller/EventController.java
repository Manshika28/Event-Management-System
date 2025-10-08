package com.example.eventservice.controller;

import com.example.eventservice.model.Event;
import com.example.eventservice.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository repo;

    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    // Create new event
    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody Event e) {
        Event saved = repo.save(e);
        return ResponseEntity.ok(saved);
    }

    // Get single event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all events
    @GetMapping
    public List<Event> listEvents() {
        return repo.findAll();
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event e) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setTitle(e.getTitle());
                    existing.setDescription(e.getDescription());
                    existing.setDate(e.getDate()); // ✅ keep only date
                    Event updated = repo.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
