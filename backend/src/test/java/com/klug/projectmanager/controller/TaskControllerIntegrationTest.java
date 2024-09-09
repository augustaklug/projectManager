package com.klug.projectmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testCreateUpdateDeleteAndRetrieveTask() throws Exception {
        // Create a task
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Test Task");
        taskDTO.setDeadline(LocalDate.now().plusDays(7));
        taskDTO.setStatus("To Do");

        String responseString = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andReturn().getResponse().getContentAsString();

        TaskDTO createdTask = objectMapper.readValue(responseString, TaskDTO.class);

        // Update the task
        createdTask.setName("Updated Test Task");
        mockMvc.perform(put("/api/tasks/" + createdTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Task"));

        // Retrieve the task
        mockMvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Task"));

        // Delete the task
        mockMvc.perform(delete("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNoContent());

        // Verify the task is deleted (should return 404)
        mockMvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testAssignTaskToUser() throws Exception {
        // Create a task
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Assigned Task");
        taskDTO.setDeadline(LocalDate.now().plusDays(7));
        taskDTO.setStatus("To Do");

        String responseString = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TaskDTO createdTask = objectMapper.readValue(responseString, TaskDTO.class);

        // Assign the task to a user
        createdTask.setAssignedToId(testUser.getId());
        mockMvc.perform(put("/api/tasks/" + createdTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedToId").value(testUser.getId()));
    }
}