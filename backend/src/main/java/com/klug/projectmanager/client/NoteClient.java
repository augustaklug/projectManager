package com.klug.projectmanager.client;

import com.klug.projectmanager.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "note-service")
public interface NoteClient {

    @PostMapping("/api/notes")
    NoteDTO createNote(@RequestBody NoteDTO note);

    @PutMapping("/api/notes/{id}")
    NoteDTO updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDetails);

    @DeleteMapping("/api/notes/{id}")
    void deleteNote(@PathVariable Long id);

    @GetMapping("/api/notes/{id}")
    NoteDTO getNoteById(@PathVariable Long id);

    @GetMapping("/api/notes/task/{taskId}")
    List<NoteDTO> getNotesByTaskId(@PathVariable Long taskId);

    @GetMapping("/api/notes/project/{projectId}")
    List<NoteDTO> getNotesByProjectId(@PathVariable Long projectId);
}