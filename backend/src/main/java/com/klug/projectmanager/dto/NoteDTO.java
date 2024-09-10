package com.klug.projectmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NoteDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    @NotBlank
    private String content;

    private LocalDateTime lastUpdated;

    private Long taskId;

    private Long projectId;
}
