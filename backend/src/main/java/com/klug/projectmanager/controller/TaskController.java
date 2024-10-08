package com.klug.projectmanager.controller;

import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.dto.NoteDTO;
import com.klug.projectmanager.entity.TaskHistory;
import com.klug.projectmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskDTO>> getUserTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        List<TaskDTO> tasks = taskService.getTasksByUser(currentUsername);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TaskHistory>> getTaskHistory(@PathVariable Long id) {
        List<TaskHistory> history = taskService.getTaskHistory(id);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

     // Criar uma nota para uma tarefa específica
    @PostMapping("/{taskId}/notes")
    public ResponseEntity<NoteDTO> addNoteToTask(@PathVariable Long taskId, @Valid @RequestBody NoteDTO noteDTO) {
        NoteDTO createdNote = taskService.addNoteToTask(taskId, noteDTO);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    // Listar todas as notas para uma tarefa específica
    @GetMapping("/{taskId}/notes")
    public ResponseEntity<List<NoteDTO>> getNotesForTask(@PathVariable Long taskId) {
        List<NoteDTO> notes = taskService.getTaskNotes(taskId);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    // Atualizar uma nota de uma tarefa específica
    @PutMapping("/{taskId}/notes/{noteId}")
    public ResponseEntity<NoteDTO> updateTaskNote(@PathVariable Long taskId, @PathVariable Long noteId, @Valid @RequestBody NoteDTO noteDTO) {
        NoteDTO updatedNote = taskService.updateTaskNote(taskId, noteId, noteDTO);
        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
    }

    // Deletar uma nota de uma tarefa específica
    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<Void> deleteTaskNote(@PathVariable Long noteId) {
        taskService.deleteTaskNote(noteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}