package com.klug.noteservice.service;

import com.klug.noteservice.entity.Note;
import com.klug.noteservice.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNote() {
        Note note = new Note();
        note.setContent("Test note");
        note.setTaskId(1L);

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note createdNote = noteService.createNote(note);

        assertNotNull(createdNote);
        assertEquals("Test note", createdNote.getContent());
        assertNotNull(createdNote.getLastUpdated());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testUpdateNote() {
        Long noteId = 1L;
        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setContent("Original content");

        Note updatedNote = new Note();
        updatedNote.setContent("Updated content");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        Note result = noteService.updateNote(noteId, updatedNote);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testDeleteNote() {
        Long noteId = 1L;
        doNothing().when(noteRepository).deleteById(noteId);

        noteService.deleteNote(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    void testGetNoteById() {
        Long noteId = 1L;
        Note note = new Note();
        note.setId(noteId);
        note.setContent("Test note");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById(noteId);

        assertNotNull(result);
        assertEquals(noteId, result.getId());
        assertEquals("Test note", result.getContent());
        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetNotesByTaskId() {
        Long taskId = 1L;
        List<Note> notes = Arrays.asList(
            new Note(1L, "Note 1", LocalDateTime.now(), taskId, null),
            new Note(2L, "Note 2", LocalDateTime.now(), taskId, null)
        );

        when(noteRepository.findByTaskId(taskId)).thenReturn(notes);

        List<Note> result = noteService.getNotesByTaskId(taskId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findByTaskId(taskId);
    }

    @Test
    void testGetNotesByProjectId() {
        Long projectId = 1L;
        List<Note> notes = Arrays.asList(
            new Note(1L, "Note 1", LocalDateTime.now(), null, projectId),
            new Note(2L, "Note 2", LocalDateTime.now(), null, projectId)
        );

        when(noteRepository.findByProjectId(projectId)).thenReturn(notes);

        List<Note> result = noteService.getNotesByProjectId(projectId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findByProjectId(projectId);
    }

    @Test
    void testUpdateNoteNotFound() {
        Long noteId = 1L;
        Note updatedNote = new Note();
        updatedNote.setContent("Updated content");

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> noteService.updateNote(noteId, updatedNote));
        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void testGetNoteByIdNotFound() {
        Long noteId = 1L;

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> noteService.getNoteById(noteId));
        verify(noteRepository, times(1)).findById(noteId);
    }
}
