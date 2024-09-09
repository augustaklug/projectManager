package com.klug.projectmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klug.projectmanager.dto.AuthRequest;
import com.klug.projectmanager.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterAndAuthenticateUser() throws Exception {
        // Generate a unique username
        String uniqueUsername = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        // Register a new user
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(uniqueUsername);
        signUpRequest.setEmail(uniqueUsername + "@example.com");
        signUpRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        // Authenticate the user
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(uniqueUsername);
        authRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}