package com.eventpilot.backend.security;

import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.UserRole;
import com.eventpilot.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldLoadUserByEmail() {
        User user = new User(
                "Test User",
                "test@example.com",
                "hashed-password",
                UserRole.USER
        );

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        CustomUserDetailsService service =
                new CustomUserDetailsService(userRepository);

        UserDetails result =
                service.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", result.getUsername());
        assertEquals("hashed-password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void shouldLoadOrganizerRole() {
        User user = new User(
                "Organizer",
                "organizer@example.com",
                "hashed-password",
                UserRole.ORGANIZER
        );

        when(userRepository.findByEmail("organizer@example.com"))
                .thenReturn(Optional.of(user));

        CustomUserDetailsService service =
                new CustomUserDetailsService(userRepository);

        UserDetails result =
                service.loadUserByUsername("organizer@example.com");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ORGANIZER")));
    }

    @Test
    void shouldLoadAdminRole() {
        User user = new User(
                "Admin",
                "admin@example.com",
                "hashed-password",
                UserRole.ADMIN
        );

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(user));

        CustomUserDetailsService service =
                new CustomUserDetailsService(userRepository);

        UserDetails result =
                service.loadUserByUsername("admin@example.com");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        CustomUserDetailsService service =
                new CustomUserDetailsService(userRepository);

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com")
        );

        verify(userRepository).findByEmail("missing@example.com");
    }
}