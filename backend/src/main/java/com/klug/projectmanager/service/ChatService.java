package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.ChatMessageDTO;
import com.klug.projectmanager.entity.ChatMessage;
import com.klug.projectmanager.entity.Project;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.ChatMessageRepository;
import com.klug.projectmanager.repository.ProjectRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatMessageDTO sendMessage(ChatMessageDTO messageDTO) {
        // Validar se o projeto existe
        Optional<Project> projectOptional = projectRepository.findById(messageDTO.getProjectId());
        if (projectOptional.isEmpty()) {
            throw new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Validar se o usuário existe
        Optional<User> userOptional = userRepository.findById(messageDTO.getSenderId());
        if (userOptional.isEmpty()) {
            throw new CustomException("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Mapear o DTO para uma entidade ChatMessage
        ChatMessage message = mapToEntity(messageDTO);
        message.setSentAt(LocalDateTime.now());

        // Salvar a mensagem no banco de dados
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Mapear a entidade salva de volta para um DTO
        return mapToDTO(savedMessage);
    }

    public List<ChatMessageDTO> getMessagesForProject(Long projectId) {
        List<ChatMessage> messages = chatMessageRepository.findByProjectId(projectId);
        return messages.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO mapToDTO(ChatMessage message) {
        return modelMapper.map(message, ChatMessageDTO.class);
    }

    private ChatMessage mapToEntity(ChatMessageDTO messageDTO) {
        return modelMapper.map(messageDTO, ChatMessage.class);
    }
}