package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void shouldSaveAndFindVenueById() {
        Venue venue = new Venue(
                "Test Venue",
                "123 Main Street",
                "Mumbai",
                "Maharashtra",
                "India",
                "400001",
                500
        );

        Venue savedVenue = venueRepository.save(venue);

        Optional<Venue> result =
                venueRepository.findById(savedVenue.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Venue");
        assertThat(result.get().getCity()).isEqualTo("Mumbai");
        assertThat(result.get().getCapacity()).isEqualTo(500);
    }

    @Test
    void shouldDeleteVenue() {
        Venue venue = new Venue(
                "Delete Venue",
                "456 Test Street",
                "Mumbai",
                "Maharashtra",
                "India",
                "400002",
                200
        );

        Venue savedVenue = venueRepository.save(venue);

        venueRepository.deleteById(savedVenue.getId());

        Optional<Venue> result =
                venueRepository.findById(savedVenue.getId());

        assertThat(result).isEmpty();
    }
}