package com.klug.noteservice.service;

import com.klug.noteservice.entity.Note;
import com.klug.noteservice.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(Note note) {
        note.setLastUpdated(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note noteDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));

        note.setContent(noteDetails.getContent());
        note.setLastUpdated(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id " + id));
    }

    public List<Note> getNotesByTaskId(Long taskId) {
        return noteRepository.findByTaskId(taskId);
    }

    public List<Note> getNotesByProjectId(Long projectId) {
        return noteRepository.findByProjectId(projectId);
    }
}