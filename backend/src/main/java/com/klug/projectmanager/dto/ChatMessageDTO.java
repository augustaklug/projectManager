package com.klug.projectmanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChatMessageDTO {
    private Long id;
    private Long projectId;
    private Long senderId;
    private String content;
    private LocalDate sentAt;
}