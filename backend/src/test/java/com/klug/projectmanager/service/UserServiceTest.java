package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.UserDTO;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.entity.UserHistory;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.UserHistoryRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserHistoryRepository userHistoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUserAndHistory() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(null);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userHistoryRepository, times(1)).save(any(UserHistory.class));
    }

    @Test
    void updateUser_ShouldUpdateUserAndCreateHistory() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updated@example.com");
        userDTO.setRole("ROLE_ADMIN");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("originaluser");
        existingUser.setEmail("original@example.com");
        existingUser.setRole("ROLE_USER");
        existingUser.setDeleted(false);  // Adicione esta linha

        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(modelMapper.map(existingUser, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.updateUser(userId, userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        verify(userHistoryRepository, times(3)).save(any(UserHistory.class)); // Username, email, and role changes
    }

    @Test
    void updateUser_UserNotFound_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.updateUser(userId, userDTO));
        assertEquals("Usuário não encontrado.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }


    @Test
    void getUserHistory_ShouldReturnHistoryList() {
        // Arrange
        Long userId = 1L;
        List<UserHistory> historyList = Arrays.asList(
                new UserHistory(), new UserHistory()
        );
        when(userHistoryRepository.findByUserIdOrderByChangeDateDesc(userId)).thenReturn(historyList);

        // Act
        List<UserHistory> result = userService.getUserHistory(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteUserById_ShouldSoftDeleteUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("userToDelete");
        user.setDeleted(false);

        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUserById(userId);

        // Assert
        assertTrue(user.isDeleted());
        verify(userRepository, times(1)).save(user);
        verify(userHistoryRepository, times(1)).save(any(UserHistory.class));
    }

    @Test
    void getUserByUsername_ShouldReturnUser() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@example.com");

        when(userRepository.getByUsernameAndIsDeletedFalse(username)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(new UserDTO());

        // Act
        UserDTO result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).getByUsernameAndIsDeletedFalse(username);
    }

    @Test
    void getUserByUsername_UserNotFound_ShouldThrowException() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.getByUsernameAndIsDeletedFalse(username)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.getUserByUsername(username));
        assertEquals("Usuário não encontrado.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
