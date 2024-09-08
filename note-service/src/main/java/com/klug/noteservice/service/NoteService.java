package com.klug.noteservice.service;

import com.klug.noteservice.dto.NoteDTO;
import com.klug.noteservice.entity.Note;
import com.klug.noteservice.repository.NoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ModelMapper modelMapper;

    public NoteDTO createNote(NoteDTO noteDTO) {
        Note note = modelMapper.map(noteDTO, Note.class);
        note.setLastUpdated(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);
        return modelMapper.map(savedNote, NoteDTO.class);
    }

    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        // Atualize os campos, mas mantenha o ID original
        modelMapper.map(noteDTO, existingNote);
        existingNote.setId(id);
        existingNote.setLastUpdated(LocalDateTime.now());

        Note updatedNote = noteRepository.save(existingNote);
        return modelMapper.map(updatedNote, NoteDTO.class);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public NoteDTO getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        return modelMapper.map(note, NoteDTO.class);
    }

    public List<NoteDTO> getNotesByTaskId(Long taskId) {
        List<Note> notes = noteRepository.findByTaskId(taskId);
        return notes.stream()
                .map(note -> modelMapper.map(note, NoteDTO.class))
                .collect(Collectors.toList());
    }

    public List<NoteDTO> getNotesByProjectId(Long projectId) {
        List<Note> notes = noteRepository.findByProjectId(projectId);
        return notes.stream()
                .map(note -> modelMapper.map(note, NoteDTO.class))
                .collect(Collectors.toList());
    }
}