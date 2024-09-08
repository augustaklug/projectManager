package com.klug.projectmanager.integration;

import com.klug.projectmanager.messaging.NoteMessageSender;
import com.klug.projectmanager.dto.NoteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class NoteServiceIntegrationTest {

    @Autowired
    private NoteMessageSender noteMessageSender;

    @Test
    public void testCreateAndGetNote() {
        // Arrange
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setContent("Test Note");
        noteDTO.setProjectId(1L);

        // Act
        NoteDTO createdNote = noteMessageSender.createNote(noteDTO);

        // Assert
        assertNotNull(createdNote);
        assertNotNull(createdNote.getId());
        assertEquals("Test Note", createdNote.getContent());
        assertEquals(1L, createdNote.getProjectId());

        // Get the created note
        NoteDTO retrievedNote = noteMessageSender.getNoteById(createdNote.getId());
        assertEquals(createdNote.getId(), retrievedNote.getId());
        assertEquals(createdNote.getContent(), retrievedNote.getContent());
    }

    @Test
    public void testGetNotesByProjectId() {
        // Arrange
        Long projectId = 1L;
        NoteDTO noteDTO1 = new NoteDTO();
        noteDTO1.setContent("Note 1");
        noteDTO1.setProjectId(projectId);
        NoteDTO noteDTO2 = new NoteDTO();
        noteDTO2.setContent("Note 2");
        noteDTO2.setProjectId(projectId);

        noteMessageSender.createNote(noteDTO1);
        noteMessageSender.createNote(noteDTO2);

        // Act
        List<NoteDTO> notes = noteMessageSender.getNotesByProjectId(projectId);

        // Assert
        assertNotNull(notes);
        assertTrue(notes.size() >= 2);
        assertTrue(notes.stream().anyMatch(note -> "Note 1".equals(note.getContent())));
        assertTrue(notes.stream().anyMatch(note -> "Note 2".equals(note.getContent())));
    }

    @Test
    public void testUpdateNote() {
        // Arrange
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setContent("Original Note");
        noteDTO.setProjectId(1L);
        NoteDTO createdNote = noteMessageSender.createNote(noteDTO);

        // Act
        createdNote.setContent("Updated Note");
        NoteDTO updatedNote = noteMessageSender.updateNote(createdNote.getId(), createdNote);

        // Assert
        assertNotNull(updatedNote);
        assertEquals(createdNote.getId(), updatedNote.getId());
        assertEquals("Updated Note", updatedNote.getContent());
    }

    @Test
    public void testDeleteNote() {
        // Arrange
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setContent("Note to Delete");
        noteDTO.setProjectId(1L);
        NoteDTO createdNote = noteMessageSender.createNote(noteDTO);

        // Act & Assert
        assertDoesNotThrow(() -> noteMessageSender.deleteNote(createdNote.getId()));

        // Verify deletion
        assertThrows(RuntimeException.class, () -> noteMessageSender.getNoteById(createdNote.getId()));
    }
}