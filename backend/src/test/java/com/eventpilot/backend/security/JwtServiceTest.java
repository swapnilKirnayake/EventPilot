package com.eventpilot.backend.security;

import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            "eventpilot-local-dev-jwt-secret-2026";

    private static final long EXPIRATION_MS = 86_400_000L;

    @Test
    void shouldGenerateAndReadToken() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

        User user = new User(
                "Test User",
                "test@example.com",
                "password",
                UserRole.USER
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("test@example.com", jwtService.extractUsername(token));
        assertEquals("USER", jwtService.extractRole(token));
    }

    @Test
    void shouldValidateTokenForMatchingUser() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

        User user = new User(
                "Test User",
                "test@example.com",
                "password",
                UserRole.USER
        );

        String token = jwtService.generateToken(user);

        UserDetails userDetails =
        org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldRejectTokenForDifferentUser() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

        User originalUser = new User(
                "Original User",
                "original@example.com",
                "password",
                UserRole.USER
        );

        User differentUser = new User(
                "Different User",
                "different@example.com",
                "password",
                UserRole.USER
        );

        String token = jwtService.generateToken(originalUser);

        UserDetails differentUserDetails =
        org.springframework.security.core.userdetails.User
                .withUsername(differentUser.getEmail())
                .password(differentUser.getPassword())
                .roles(differentUser.getRole().name())
                .build();

assertFalse(jwtService.isTokenValid(token, differentUserDetails));
    }

    @Test
    void shouldIncludeOrganizerRoleInToken() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

        User user = new User(
                "Organizer",
                "organizer@example.com",
                "password",
                UserRole.ORGANIZER
        );

        String token = jwtService.generateToken(user);

        assertEquals("ORGANIZER", jwtService.extractRole(token));
    }

    @Test
    void shouldIncludeAdminRoleInToken() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION_MS);

        User user = new User(
                "Admin",
                "admin@example.com",
                "password",
                UserRole.ADMIN
        );

        String token = jwtService.generateToken(user);

        assertEquals("ADMIN", jwtService.extractRole(token));
    }

    @Test
    void shouldRejectShortSecret() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JwtService("short-secret", EXPIRATION_MS)
        );

        assertEquals(
                "JWT secret must be at least 32 bytes long",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankSecret() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JwtService("   ", EXPIRATION_MS)
        );

        assertEquals(
                "JWT secret must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectInvalidExpiration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JwtService(SECRET, 0)
        );

        assertEquals(
                "JWT expiration must be greater than zero",
                exception.getMessage()
        );
    }
}