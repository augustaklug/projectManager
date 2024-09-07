package com.klug.noteservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klug.noteservice.entity.Note;
import com.klug.noteservice.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Note testNote;

    @BeforeEach
    void setUp() {
        testNote = new Note();
        testNote.setId(1L);
        testNote.setContent("Test note content");
        testNote.setLastUpdated(LocalDateTime.now());
        testNote.setTaskId(1L);
        testNote.setProjectId(1L);
    }

    @Test
    void testCreateNote() throws Exception {
        when(noteService.createNote(any(Note.class))).thenReturn(testNote);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testNote.getId()))
                .andExpect(jsonPath("$.content").value(testNote.getContent()));
    }

    @Test
    void testUpdateNote() throws Exception {
        when(noteService.updateNote(eq(1L), any(Note.class))).thenReturn(testNote);

        mockMvc.perform(put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testNote.getId()))
                .andExpect(jsonPath("$.content").value(testNote.getContent()));
    }

    @Test
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNoteById() throws Exception {
        when(noteService.getNoteById(1L)).thenReturn(testNote);

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testNote.getId()))
                .andExpect(jsonPath("$.content").value(testNote.getContent()));
    }

    @Test
    void testGetNotesByTaskId() throws Exception {
        List<Note> notes = Arrays.asList(testNote);
        when(noteService.getNotesByTaskId(1L)).thenReturn(notes);

        mockMvc.perform(get("/api/notes/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testNote.getId()))
                .andExpect(jsonPath("$[0].content").value(testNote.getContent()));
    }

    @Test
    void testGetNotesByProjectId() throws Exception {
        List<Note> notes = Arrays.asList(testNote);
        when(noteService.getNotesByProjectId(1L)).thenReturn(notes);

        mockMvc.perform(get("/api/notes/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testNote.getId()))
                .andExpect(jsonPath("$[0].content").value(testNote.getContent()));
    }

    @Test
    void testGetNoteByIdNotFound() throws Exception {
        when(noteService.getNoteById(99L)).thenThrow(new RuntimeException("Note not found"));

        mockMvc.perform(get("/api/notes/99"))
                .andExpect(status().isNotFound());
    }
}