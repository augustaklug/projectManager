package com.klug.noteservice.service;

import com.klug.noteservice.dto.NoteDTO;
import com.klug.noteservice.entity.Note;
import com.klug.noteservice.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNote_ShouldCreateNoteAndReturnDTO() {
        // Arrange
        NoteDTO inputDTO = new NoteDTO();
        inputDTO.setContent("Test note");
        inputDTO.setProjectId(1L);

        Note note = new Note();
        note.setId(1L);
        note.setContent("Test note");

        NoteDTO outputDTO = new NoteDTO();
        outputDTO.setId(1L);
        outputDTO.setContent("Test note");
        outputDTO.setProjectId(1L);

        when(modelMapper.map(inputDTO, Note.class)).thenReturn(note);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(modelMapper.map(note, NoteDTO.class)).thenReturn(outputDTO);

        // Act
        NoteDTO result = noteService.createNote(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test note", result.getContent());
        assertEquals(1L, result.getProjectId());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void updateNote_ShouldUpdateNoteAndReturnDTO() {
        // Arrange
        Long noteId = 1L;
        NoteDTO inputDTO = new NoteDTO();
        inputDTO.setContent("Updated note");

        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setContent("Original note");

        Note updatedNote = new Note();
        updatedNote.setId(noteId);
        updatedNote.setContent("Updated note");

        NoteDTO outputDTO = new NoteDTO();
        outputDTO.setId(noteId);
        outputDTO.setContent("Updated note");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(modelMapper.map(inputDTO, Note.class)).thenReturn(updatedNote);
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);
        when(modelMapper.map(updatedNote, NoteDTO.class)).thenReturn(outputDTO);

        // Act
        NoteDTO result = noteService.updateNote(noteId, inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(noteId, result.getId());
        assertEquals("Updated note", result.getContent());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void deleteNote_ShouldDeleteNote() {
        // Arrange
        Long noteId = 1L;

        // Act
        noteService.deleteNote(noteId);

        // Assert
        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    void getNoteById_ShouldReturnNoteDTO() {
        // Arrange
        Long noteId = 1L;
        Note note = new Note();
        note.setId(noteId);
        note.setContent("Test note");

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(noteId);
        noteDTO.setContent("Test note");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(modelMapper.map(note, NoteDTO.class)).thenReturn(noteDTO);

        // Act
        NoteDTO result = noteService.getNoteById(noteId);

        // Assert
        assertNotNull(result);
        assertEquals(noteId, result.getId());
        assertEquals("Test note", result.getContent());
    }

    @Test
    void getNotesByTaskId_ShouldReturnListOfNoteDTOs() {
        // Arrange
        Long taskId = 1L;
        List<Note> notes = Arrays.asList(
                new Note(1L, "Note 1", LocalDateTime.now(), taskId, null),
                new Note(2L, "Note 2", LocalDateTime.now(), taskId, null)
        );

        List<NoteDTO> noteDTOs = Arrays.asList(
                new NoteDTO(1L, "Note 1", LocalDateTime.now(), taskId, null),
                new NoteDTO(2L, "Note 2", LocalDateTime.now(), taskId, null)
        );

        when(noteRepository.findByTaskId(taskId)).thenReturn(notes);
        when(modelMapper.map(any(Note.class), eq(NoteDTO.class))).thenReturn(noteDTOs.get(0), noteDTOs.get(1));

        // Act
        List<NoteDTO> result = noteService.getNotesByTaskId(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Note 1", result.get(0).getContent());
        assertEquals("Note 2", result.get(1).getContent());
    }

    @Test
    void getNotesByProjectId_ShouldReturnListOfNoteDTOs() {
        // Arrange
        Long projectId = 1L;
        List<Note> notes = Arrays.asList(
                new Note(1L, "Note 1", LocalDateTime.now(), null, projectId),
                new Note(2L, "Note 2", LocalDateTime.now(), null, projectId)
        );

        List<NoteDTO> noteDTOs = Arrays.asList(
                new NoteDTO(1L, "Note 1", LocalDateTime.now(), null, projectId),
                new NoteDTO(2L, "Note 2", LocalDateTime.now(), null, projectId)
        );

        when(noteRepository.findByProjectId(projectId)).thenReturn(notes);
        when(modelMapper.map(any(Note.class), eq(NoteDTO.class))).thenReturn(noteDTOs.get(0), noteDTOs.get(1));

        // Act
        List<NoteDTO> result = noteService.getNotesByProjectId(projectId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Note 1", result.get(0).getContent());
        assertEquals("Note 2", result.get(1).getContent());
    }

    @Test
    void createNote_ShouldSetLastUpdated() {
        // Arrange
        NoteDTO inputDTO = new NoteDTO();
        inputDTO.setContent("Test note");
        inputDTO.setProjectId(1L);

        Note note = new Note();
        note.setId(1L);
        note.setContent("Test note");

        when(modelMapper.map(inputDTO, Note.class)).thenReturn(note);
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note savedNote = invocation.getArgument(0);
            savedNote.setId(1L);
            return savedNote;
        });
        when(modelMapper.map(any(Note.class), eq(NoteDTO.class))).thenAnswer(invocation -> {
            Note savedNote = invocation.getArgument(0);
            NoteDTO outputDTO = new NoteDTO();
            outputDTO.setId(savedNote.getId());
            outputDTO.setContent(savedNote.getContent());
            outputDTO.setLastUpdated(savedNote.getLastUpdated());
            return outputDTO;
        });

        // Act
        NoteDTO result = noteService.createNote(inputDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getLastUpdated());
        assertTrue(result.getLastUpdated().isBefore(LocalDateTime.now()) || result.getLastUpdated().isEqual(LocalDateTime.now()));
    }

    @Test
    void updateNote_ShouldUpdateLastUpdated() {
        // Arrange
        Long noteId = 1L;
        NoteDTO inputDTO = new NoteDTO();
        inputDTO.setContent("Updated note");

        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setContent("Original note");
        existingNote.setLastUpdated(LocalDateTime.now().minusDays(1));

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Note.class), eq(NoteDTO.class))).thenAnswer(invocation -> {
            Note savedNote = invocation.getArgument(0);
            NoteDTO outputDTO = new NoteDTO();
            outputDTO.setId(savedNote.getId());
            outputDTO.setContent(savedNote.getContent());
            outputDTO.setLastUpdated(savedNote.getLastUpdated());
            return outputDTO;
        });

        // Act
        NoteDTO result = noteService.updateNote(noteId, inputDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getLastUpdated());
        assertTrue(result.getLastUpdated().isAfter(existingNote.getLastUpdated()));
        assertTrue(result.getLastUpdated().isBefore(LocalDateTime.now()) || result.getLastUpdated().isEqual(LocalDateTime.now()));
    }
}