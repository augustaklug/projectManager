package com.klug.noteservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private Long id;

    @NotBlank
    private String content;

    private LocalDateTime lastUpdated;

    private Long taskId;

    private Long projectId;
}
