package com.klug.projectmanager.messaging;

import com.klug.projectmanager.config.RabbitMQConfig;
import com.klug.projectmanager.dto.NoteDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteMessageSender {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public NoteMessageSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public NoteDTO createNote(NoteDTO note) {
        return (NoteDTO) amqpTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_CREATE_NOTE,
                note
        );
    }

    public NoteDTO updateNote(Long id, NoteDTO noteDetails) {
        noteDetails.setId(id);
        return (NoteDTO) amqpTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_UPDATE_NOTE,
                noteDetails
        );
    }

    public void deleteNote(Long id) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_DELETE_NOTE,
                id
        );
    }

    public NoteDTO getNoteById(Long id) {
        return (NoteDTO) amqpTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_GET_NOTE,
                id
        );
    }

    public List<NoteDTO> getNotesByTaskId(Long taskId) {
        return (List<NoteDTO>) amqpTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_GET_NOTES_BY_TASK,
                taskId
        );
    }

    public List<NoteDTO> getNotesByProjectId(Long projectId) {
        return (List<NoteDTO>) amqpTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NOTES,
                RabbitMQConfig.ROUTING_KEY_GET_NOTES_BY_PROJECT,
                projectId
        );
    }
}