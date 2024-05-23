package com.klug.projectmanager.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long[] teamMemberIds;
    private List<TaskDTO> tasks;
    private List<NoteDTO> notes;
    private List<ChatMessageDTO> chatMessages;
}
