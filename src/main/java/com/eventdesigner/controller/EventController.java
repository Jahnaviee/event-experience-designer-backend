package com.eventdesigner.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventdesigner.backend.entity.Event;
import com.eventdesigner.backend.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5176",
    "https://event-experience-designer-frontend.onrender.com"
})
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @PathVariable("id") Long id) {

        Optional<Event> event = eventRepository.findById(id);

        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {

        if (event.getTitle() == null
                || event.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Event title is required.");
        }

        event.setId(null);

        if (event.getOwnerId() == null) {
            event.setOwnerId(1L);
        }

        if (event.getPublicSlug() == null
                || event.getPublicSlug().trim().isEmpty()) {

            String slug = event.getTitle()
                    .trim()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "-")
                    .replaceAll("(^-+|-+$)", "");

            if (slug.isEmpty()) {
                slug = "event";
            }

            event.setPublicSlug(
                    slug + "-" + System.currentTimeMillis()
            );
        }

        if (event.getPublished() == null) {
            event.setPublished(false);
        }

        Event savedEvent = eventRepository.save(event);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEvent);
    }

    // Update an existing event
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable("id") Long id,
            @RequestBody Event updatedEvent) {

        Optional<Event> result = eventRepository.findById(id);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (updatedEvent.getTitle() == null
                || updatedEvent.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Event title is required.");
        }

        Event existingEvent = result.get();

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setLocation(updatedEvent.getLocation());

        if (updatedEvent.getPublicSlug() != null
                && !updatedEvent.getPublicSlug().trim().isEmpty()) {
            existingEvent.setPublicSlug(updatedEvent.getPublicSlug());
        }

        if (updatedEvent.getPublished() != null) {
            existingEvent.setPublished(updatedEvent.getPublished());
        }

        Event savedEvent = eventRepository.save(existingEvent);

        return ResponseEntity.ok(savedEvent);
    }

    // Delete an existing event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("id") Long id) {

        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        eventRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
