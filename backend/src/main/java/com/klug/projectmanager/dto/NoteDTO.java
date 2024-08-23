package com.klug.projectmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDTO {

    private Long id;

    @NotBlank
    private String content;

    private LocalDateTime lastUpdated;

    private Long taskId;

    private Long projectId;
}
