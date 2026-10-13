
package com.eventdesigner.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.eventdesigner.backend.entity.Event;
import com.eventdesigner.backend.entity.VenueElement;
import com.eventdesigner.backend.repository.EventRepository;
import com.eventdesigner.backend.repository.VenueElementRepository;

@RestController
@RequestMapping("/api/events/{eventId}/venue-elements")
public class VenueElementController {

    private final VenueElementRepository venueElementRepository;
    private final EventRepository eventRepository;

    public VenueElementController(
            VenueElementRepository venueElementRepository,
            EventRepository eventRepository) {
        this.venueElementRepository = venueElementRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<VenueElement> getVenueElements(
            @PathVariable Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Event not found");
        }

        return venueElementRepository.findByEventId(eventId);
    }

    @PutMapping
    @Transactional
    public List<VenueElement> saveVenueElements(
            @PathVariable Long eventId,
            @RequestBody List<VenueElement> elements) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event not found"));

        for (VenueElement element : elements) {
            if (element.getType() == null
                    || element.getType().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Every venue element must have a type");
            }

            element.setId(null);
            element.setEventId(event.getId());
        }

        venueElementRepository.deleteByEventId(eventId);

        return venueElementRepository.saveAll(elements);
    }
}
