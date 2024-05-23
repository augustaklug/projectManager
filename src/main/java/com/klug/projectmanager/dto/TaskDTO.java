package com.klug.projectmanager.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private Long projectId;
    private LocalDate deadline;
    private String status;
    private Long assignedToId;
    private List<NoteDTO> notes;
}
