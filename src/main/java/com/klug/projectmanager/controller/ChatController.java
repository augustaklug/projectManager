package com.klug.projectmanager.controller;

import com.klug.projectmanager.dto.ChatMessageDTO;
import com.klug.projectmanager.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatMessageDTO> sendMessage(@Valid @RequestBody ChatMessageDTO messageDTO) {
        ChatMessageDTO sentMessage = chatService.sendMessage(messageDTO);
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessagesForProject(@PathVariable Long projectId) {
        List<ChatMessageDTO> messages = chatService.getMessagesForProject(projectId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Outros métodos relacionados ao chat, se necessário
}
