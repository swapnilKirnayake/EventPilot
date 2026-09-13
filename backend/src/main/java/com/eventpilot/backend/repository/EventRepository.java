package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Event;
import com.eventpilot.backend.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    List<Event> findByOrganizerId(Long organizerId);
}