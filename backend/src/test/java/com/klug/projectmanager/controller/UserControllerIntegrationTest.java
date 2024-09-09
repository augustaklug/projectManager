package com.klug.projectmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klug.projectmanager.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUpdateDeleteAndRetrieveUser() throws Exception {
        // Generate a unique username
        String uniqueUsername = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        // Create a user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(uniqueUsername);
        userDTO.setEmail(uniqueUsername + "@example.com");
        userDTO.setPassword("password");
        userDTO.setRole("ROLE_USER");

        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(uniqueUsername))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        UserDTO createdUser = objectMapper.readValue(responseString, UserDTO.class);

        // Update the user
        createdUser.setEmail("updated" + uniqueUsername + "@example.com");
        mockMvc.perform(put("/api/users/" + createdUser.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated" + uniqueUsername + "@example.com"));

        // Retrieve the user
        mockMvc.perform(get("/api/users/username/" + createdUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated" + uniqueUsername + "@example.com"));

        // Delete the user
        mockMvc.perform(delete("/api/users/" + createdUser.getUsername()))
                .andExpect(status().isNoContent());

        // Verify the user is deleted (should return 404)
        mockMvc.perform(get("/api/users/username/" + createdUser.getUsername()))
                .andExpect(status().isNotFound());
    }
}