package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Booking;
import com.eventpilot.backend.entity.Event;
import com.eventpilot.backend.entity.Ticket;
import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.BookingStatus;
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
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldSaveAndFindBookingsByUserId() {
        User organizer = new User(
                "Booking Organizer",
                "booking-organizer@example.com",
                "password",
                UserRole.ORGANIZER
        );

        organizer = userRepository.save(organizer);

        User customer = new User(
                "Booking Customer",
                "booking-customer@example.com",
                "password",
                UserRole.USER
        );

        customer = userRepository.save(customer);

        Event event = new Event(
                "Booking Event",
                "Event for booking repository testing",
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
                "Standard ticket",
                new BigDecimal("1000.00"),
                100
        );

        ticket = ticketRepository.save(ticket);

        Booking booking = new Booking(
                customer,
                ticket,
                2,
                new BigDecimal("2000.00"),
                BookingStatus.CONFIRMED
        );

        bookingRepository.save(booking);

        List<Booking> bookings =
                bookingRepository.findByUserId(customer.getId());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getQuantity()).isEqualTo(2);
        assertThat(bookings.get(0).getTotalAmount())
                .isEqualByComparingTo(new BigDecimal("2000.00"));
        assertThat(bookings.get(0).getStatus())
                .isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldFindBookingsByStatus() {
        User organizer = new User(
                "Status Organizer",
                "status-organizer@example.com",
                "password",
                UserRole.ORGANIZER
        );

        organizer = userRepository.save(organizer);

        User customer = new User(
                "Status Customer",
                "status-customer@example.com",
                "password",
                UserRole.USER
        );

        customer = userRepository.save(customer);

        Event event = new Event(
                "Status Event",
                "Event for booking status testing",
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(2).plusHours(2),
                EventStatus.PUBLISHED,
                organizer,
                null
        );

        event = eventRepository.save(event);

        Ticket ticket = new Ticket(
                event,
                "Standard",
                "Standard ticket",
                new BigDecimal("500.00"),
                50
        );

        ticket = ticketRepository.save(ticket);

        Booking booking = new Booking(
                customer,
                ticket,
                1,
                new BigDecimal("500.00"),
                BookingStatus.PENDING
        );

        bookingRepository.save(booking);

        List<Booking> bookings =
                bookingRepository.findByStatus(BookingStatus.PENDING);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getStatus())
                .isEqualTo(BookingStatus.PENDING);
    }
}