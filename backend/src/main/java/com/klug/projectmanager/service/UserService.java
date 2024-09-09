package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.UserDTO;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.entity.UserHistory;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.UserRepository;
import com.klug.projectmanager.repository.UserHistoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByUsernameAndIsDeletedFalse(userDTO.getUsername()) != null) {
            throw new CustomException("O nome de usuário já está em uso.", HttpStatus.BAD_REQUEST);
        }

        User user = mapToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDeleted(false);
        User savedUser = userRepository.save(user);

        addToHistory(savedUser, "Criação", null, "Usuário criado");

        return mapToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByIsDeletedFalse();
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        try {
            if (!Objects.equals(user.getUsername(), userDTO.getUsername())) {
                addToHistory(user, "username", user.getUsername(), userDTO.getUsername());
                user.setUsername(userDTO.getUsername());
            }
            if (!Objects.equals(user.getEmail(), userDTO.getEmail())) {
                addToHistory(user, "email", user.getEmail(), userDTO.getEmail());
                user.setEmail(userDTO.getEmail());
            }
            if (!Objects.equals(user.getRole(), userDTO.getRole())) {
                addToHistory(user, "role", user.getRole(), userDTO.getRole());
                user.setRole(userDTO.getRole());
            }

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                addToHistory(user, "password", "********", "********");
            }

            User updatedUser = userRepository.save(user);
            return mapToDTO(updatedUser);
        } catch (OptimisticLockingFailureException e) {
            throw new CustomException("O usuário foi modificado por outra operação. Por favor, tente novamente.", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
        user.setDeleted(true);
        userRepository.save(user);
        addToHistory(user, "isDeleted", "false", "true");
    }

    private void addToHistory(User user, String fieldName, String oldValue, String newValue) {
        UserHistory history = new UserHistory();
        history.setUser(user);
        history.setFieldName(fieldName);
        history.setOldValue(oldValue != null ? oldValue : "null");
        history.setNewValue(newValue != null ? newValue : "null");
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy(getCurrentUsername());
        userHistoryRepository.save(history);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Sistema";
    }

    private UserDTO mapToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User mapToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public List<UserHistory> getUserHistory(Long userId) {
        return userHistoryRepository.findByUserIdOrderByChangeDateDesc(userId);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.getByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO updateUserByUsername(String username, UserDTO userDTO) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (user == null) {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        try {
            if (!Objects.equals(user.getEmail(), userDTO.getEmail())) {
                addToHistory(user, "email", user.getEmail(), userDTO.getEmail());
                user.setEmail(userDTO.getEmail());
            }
            if (!Objects.equals(user.getRole(), userDTO.getRole())) {
                addToHistory(user, "role", user.getRole(), userDTO.getRole());
                user.setRole(userDTO.getRole());
            }

            User updatedUser = userRepository.save(user);
            return mapToDTO(updatedUser);
        } catch (OptimisticLockingFailureException e) {
            throw new CustomException("O usuário foi modificado por outra operação. Por favor, tente novamente.", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (user == null) {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
        user.setDeleted(true);
        userRepository.save(user);
        addToHistory(user, "isDeleted", "false", "true");
    }

    public List<UserHistory> getUserHistoryByUsername(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (user == null) {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
        return userHistoryRepository.findByUserIdOrderByChangeDateDesc(user.getId());
    }
}