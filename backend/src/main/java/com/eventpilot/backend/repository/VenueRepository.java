package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}