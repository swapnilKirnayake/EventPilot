package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {
        User user = new User(
                "Test User",
                "test@example.com",
                "password",
                UserRole.USER
        );

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void shouldCheckIfEmailExists() {
        User user = new User(
                "Existing User",
                "existing@example.com",
                "password",
                UserRole.USER
        );

        userRepository.save(user);

        assertThat(userRepository.existsByEmail("existing@example.com"))
                .isTrue();

        assertThat(userRepository.existsByEmail("missing@example.com"))
                .isFalse();
    }
}