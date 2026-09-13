package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Event;
import com.eventpilot.backend.entity.Ticket;
import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.EventStatus;
import com.eventpilot.backend.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindTicketsByEventId() {
        User organizer = new User(
                "Ticket Organizer",
                "ticket-organizer@example.com",
                "password",
                UserRole.ORGANIZER
        );

        organizer = userRepository.save(organizer);

        Event event = new Event(
                "Ticket Event",
                "Event for ticket repository testing",
                OffsetDateTime.now().plusDays(1),
                OffsetDateTime.now().plusDays(1).plusHours(3),
                EventStatus.PUBLISHED,
                organizer,
                null
        );

        event = eventRepository.save(event);

        Ticket ticket = new Ticket(
                event,
                "General Admission",
                "Standard event ticket",
                new BigDecimal("999.00"),
                100
        );

        ticketRepository.save(ticket);

        List<Ticket> tickets =
                ticketRepository.findByEventId(event.getId());

        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getName())
                .isEqualTo("General Admission");
        assertThat(tickets.get(0).getPrice())
                .isEqualByComparingTo(new BigDecimal("999.00"));
        assertThat(tickets.get(0).getQuantity())
                .isEqualTo(100);
        assertThat(tickets.get(0).getAvailableQuantity())
                .isEqualTo(100);
    }

    @Test
    void shouldSaveTicketWithInitialAvailableQuantityEqualToQuantity() {
        User organizer = new User(
                "Another Organizer",
                "ticket-organizer2@example.com",
                "password",
                UserRole.ORGANIZER
        );

        organizer = userRepository.save(organizer);

        Event event = new Event(
                "Another Event",
                "Another event",
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(2).plusHours(2),
                EventStatus.DRAFT,
                organizer,
                null
        );

        event = eventRepository.save(event);

        Ticket ticket = new Ticket(
                event,
                "VIP",
                "VIP ticket",
                new BigDecimal("2500.00"),
                50
        );

        Ticket savedTicket = ticketRepository.save(ticket);

        assertThat(savedTicket.getQuantity()).isEqualTo(50);
        assertThat(savedTicket.getAvailableQuantity()).isEqualTo(50);
    }
}