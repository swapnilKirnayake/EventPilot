package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByEventId(Long eventId);
}