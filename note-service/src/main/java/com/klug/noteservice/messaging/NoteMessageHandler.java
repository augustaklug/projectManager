package com.klug.noteservice.messaging;

import com.klug.noteservice.dto.NoteDTO;
import com.klug.noteservice.service.NoteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteMessageHandler {

    @Autowired
    private NoteService noteService;

    @RabbitListener(queues = "#{queueCreateNote.name}")
    public NoteDTO createNote(NoteDTO noteDTO) {
        return noteService.createNote(noteDTO);
    }

    @RabbitListener(queues = "#{queueUpdateNote.name}")
    public NoteDTO updateNote(NoteDTO noteDTO) {
        return noteService.updateNote(noteDTO.getId(), noteDTO);
    }

    @RabbitListener(queues = "#{queueDeleteNote.name}")
    public void deleteNote(Long id) {
        noteService.deleteNote(id);
    }

    @RabbitListener(queues = "#{queueGetNote.name}")
    public NoteDTO getNoteById(Long id) {
        return noteService.getNoteById(id);
    }

    @RabbitListener(queues = "#{queueGetNotesByTask.name}")
    public List<NoteDTO> getNotesByTaskId(Long taskId) {
        return noteService.getNotesByTaskId(taskId);
    }

    @RabbitListener(queues = "#{queueGetNotesByProject.name}")
    public List<NoteDTO> getNotesByProjectId(Long projectId) {
        return noteService.getNotesByProjectId(projectId);
    }
}