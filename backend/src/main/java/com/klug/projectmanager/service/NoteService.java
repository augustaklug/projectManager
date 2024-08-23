package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.NoteDTO;
import com.klug.projectmanager.entity.Note;
import com.klug.projectmanager.repository.NoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ModelMapper modelMapper;

    public NoteDTO createNoteForTask(NoteDTO noteDTO) {
        Note note = mapToEntity(noteDTO);
        note.setLastUpdated(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);
        return mapToDTO(savedNote);
    }

    public NoteDTO createNoteForProject(NoteDTO noteDTO) {
        Note note = mapToEntity(noteDTO);
        note.setLastUpdated(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);
        return mapToDTO(savedNote);
    }

    public List<NoteDTO> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NoteDTO getNoteById(Long id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            return mapToDTO(noteOptional.get());
        } else {
            throw new RuntimeException("Note not found");
        }
    }

    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setContent(noteDTO.getContent());
            note.setLastUpdated(LocalDateTime.now());
            Note updatedNote = noteRepository.save(note);
            return mapToDTO(updatedNote);
        } else {
            throw new RuntimeException("Note not found");
        }
    }

    public void deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Note not found");
        }
    }

    private NoteDTO mapToDTO(Note note) {
        return modelMapper.map(note, NoteDTO.class);
    }

    private Note mapToEntity(NoteDTO noteDTO) {
        return modelMapper.map(noteDTO, Note.class);
    }
}