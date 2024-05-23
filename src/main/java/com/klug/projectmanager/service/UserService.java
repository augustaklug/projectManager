package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.UserDTO;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        // Validar se o nome de usuário é único
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new CustomException("O nome de usuário já está em uso.", HttpStatus.BAD_REQUEST);
        }

        // Mapear o DTO para uma entidade User
        User user = mapToEntity(userDTO);

        // Codificar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salvar o usuário no banco de dados
        User savedUser = userRepository.save(user);

        // Mapear a entidade salva de volta para um DTO
        return mapToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return mapToDTO(userOptional.get());
        } else {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            // Atualizar a senha se fornecida
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            User updatedUser = userRepository.save(user);
            return mapToDTO(updatedUser);
        } else {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    private UserDTO mapToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User mapToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}