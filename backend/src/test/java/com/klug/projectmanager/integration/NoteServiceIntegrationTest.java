package com.klug.projectmanager.integration;

import com.klug.projectmanager.client.NoteClient;
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
    private NoteClient noteClient;

    @Test
    public void testCreateAndGetNote() {
        // Arrange
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setContent("Test Note");
        noteDTO.setProjectId(1L);

        // Act
        NoteDTO createdNote = noteClient.createNote(noteDTO);

        // Assert
        assertNotNull(createdNote);
        assertNotNull(createdNote.getId());
        assertEquals("Test Note", createdNote.getContent());
        assertEquals(1L, createdNote.getProjectId());

        // Get the created note
        NoteDTO retrievedNote = noteClient.getNoteById(createdNote.getId());
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

        noteClient.createNote(noteDTO1);
        noteClient.createNote(noteDTO2);

        // Act
        List<NoteDTO> notes = noteClient.getNotesByProjectId(projectId);

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
        NoteDTO createdNote = noteClient.createNote(noteDTO);

        // Act
        createdNote.setContent("Updated Note");
        NoteDTO updatedNote = noteClient.updateNote(createdNote.getId(), createdNote);

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
        NoteDTO createdNote = noteClient.createNote(noteDTO);

        // Act & Assert
        assertDoesNotThrow(() -> noteClient.deleteNote(createdNote.getId()));

        // Verify deletion
        assertThrows(RuntimeException.class, () -> noteClient.getNoteById(createdNote.getId()));
    }
}