package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.SignUpRequest;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterUser() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        authService.registerUser(request);

        User user = userRepository.findByUsername("testuser");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testRegisterUserWithExistingUsername() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("existinguser");
        request.setEmail("existing@example.com");
        request.setPassword("password");

        authService.registerUser(request);

        SignUpRequest duplicateRequest = new SignUpRequest();
        duplicateRequest.setUsername("existinguser");
        duplicateRequest.setEmail("new@example.com");
        duplicateRequest.setPassword("newpassword");

        assertThrows(RuntimeException.class, () -> authService.registerUser(duplicateRequest));
    }
}
