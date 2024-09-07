package com.klug.noteservice.controller;

import com.klug.noteservice.entity.Note;
import com.klug.noteservice.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        return ResponseEntity.ok(noteService.createNote(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(noteService.getNoteById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Note>> getNotesByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(noteService.getNotesByTaskId(taskId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Note>> getNotesByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(noteService.getNotesByProjectId(projectId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}