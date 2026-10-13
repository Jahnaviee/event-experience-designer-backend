
package com.eventdesigner.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventdesigner.backend.entity.VenueElement;

public interface VenueElementRepository
        extends JpaRepository<VenueElement, Long> {

    List<VenueElement> findByEventId(Long eventId);

    void deleteByEventId(Long eventId);
}
