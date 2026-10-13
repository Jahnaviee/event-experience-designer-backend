package com.eventdesigner.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventdesigner.backend.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}