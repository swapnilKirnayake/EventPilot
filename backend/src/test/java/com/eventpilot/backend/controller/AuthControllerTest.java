package com.eventpilot.backend.controller;

import com.eventpilot.backend.entity.User;
import com.eventpilot.backend.enums.UserRole;
import com.eventpilot.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void registerShouldCreateUserAndReturnJwt() throws Exception {

        String request = """
                {
                    "name": "Test User",
                    "email": "test@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", not(emptyString())))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        org.junit.jupiter.api.Assertions.assertEquals(
                1,
                userRepository.count()
        );

        User savedUser = userRepository
                .findByEmail("test@example.com")
                .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                "test@example.com",
                savedUser.getEmail()
        );

        org.junit.jupiter.api.Assertions.assertNotEquals(
                "password123",
                savedUser.getPassword()
        );

        org.junit.jupiter.api.Assertions.assertTrue(
                passwordEncoder.matches(
                        "password123",
                        savedUser.getPassword()
                )
        );
    }

    @Test
    void registerShouldRejectDuplicateEmail() throws Exception {

        User existingUser = new User(
                "Existing User",
                "duplicate@example.com",
                passwordEncoder.encode("password123"),
                UserRole.USER
        );

        userRepository.save(existingUser);

        String request = """
                {
                    "name": "Another User",
                    "email": "duplicate@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Email is already registered"));
    }

    @Test
    void registerShouldRejectInvalidRequest() throws Exception {

        String request = """
                {
                    "name": "",
                    "email": "not-an-email",
                    "password": "short"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void loginShouldAuthenticateAndReturnJwt() throws Exception {

        User user = new User(
                "Login User",
                "login@example.com",
                passwordEncoder.encode("password123"),
                UserRole.USER
        );

        userRepository.save(user);

        String request = """
                {
                    "email": "login@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyString())))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("login@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void loginShouldRejectInvalidCredentials() throws Exception {

        User user = new User(
                "Login User",
                "wrong-password@example.com",
                passwordEncoder.encode("password123"),
                UserRole.USER
        );

        userRepository.save(user);

        String request = """
                {
                    "email": "wrong-password@example.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));
    }

    @Test
    void loginShouldRejectInvalidRequest() throws Exception {

        String request = """
                {
                    "email": "invalid-email",
                    "password": ""
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }
}
