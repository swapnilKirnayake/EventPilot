package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Event;
import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.entity.Venue;
import com.eventpilot.backend.enums.EventStatus;
import com.eventpilot.backend.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void shouldSaveAndFindEventByStatus() {
        User organizer = new User(
                "Event Organizer",
                "organizer@example.com",
                "password",
                UserRole.ORGANIZER
        );

        userRepository.save(organizer);

        Venue venue = new Venue(
                "Event Venue",
                "123 Main Street",
                "Mumbai",
                "Maharashtra",
                "India",
                "400001",
                500
        );

        venueRepository.save(venue);

        Event event = new Event(
                "Test Event",
                "Test event description",
                OffsetDateTime.now().plusDays(1),
                OffsetDateTime.now().plusDays(1).plusHours(3),
                EventStatus.PUBLISHED,
                organizer,
                venue
        );

        eventRepository.save(event);

        List<Event> events = eventRepository.findByStatus(EventStatus.PUBLISHED);

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getTitle()).isEqualTo("Test Event");
        assertThat(events.get(0).getStatus()).isEqualTo(EventStatus.PUBLISHED);
    }

    @Test
    void shouldFindEventsByOrganizerId() {
        User organizer = new User(
                "Organizer",
                "organizer2@example.com",
                "password",
                UserRole.ORGANIZER
        );

        organizer = userRepository.save(organizer);

        Event event = new Event(
                "Organizer Event",
                "Event description",
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(2).plusHours(2),
                EventStatus.DRAFT,
                organizer,
                null
        );

        eventRepository.save(event);

        List<Event> events =
                eventRepository.findByOrganizerId(organizer.getId());

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getTitle()).isEqualTo("Organizer Event");
        assertThat(events.get(0).getOrganizer().getId())
                .isEqualTo(organizer.getId());
    }
}