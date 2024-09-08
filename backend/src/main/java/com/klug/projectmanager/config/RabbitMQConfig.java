package com.klug.projectmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.klug.projectmanager.dto.NoteDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_CREATE_NOTE = "create-note-queue";
    public static final String QUEUE_UPDATE_NOTE = "update-note-queue";
    public static final String QUEUE_DELETE_NOTE = "delete-note-queue";
    public static final String QUEUE_GET_NOTE = "get-note-queue";
    public static final String QUEUE_GET_NOTES_BY_PROJECT = "get-notes-by-project-queue";
    public static final String QUEUE_GET_NOTES_BY_TASK = "get-notes-by-task-queue";

    public static final String EXCHANGE_NOTES = "notes-exchange";

    public static final String ROUTING_KEY_CREATE_NOTE = "create.note";
    public static final String ROUTING_KEY_UPDATE_NOTE = "update.note";
    public static final String ROUTING_KEY_DELETE_NOTE = "delete.note";
    public static final String ROUTING_KEY_GET_NOTE = "get.note";
    public static final String ROUTING_KEY_GET_NOTES_BY_PROJECT = "get.notes.project";
    public static final String ROUTING_KEY_GET_NOTES_BY_TASK = "get.notes.task";

    @Bean
    public Queue queueCreateNote() {
        return new Queue(QUEUE_CREATE_NOTE);
    }

    @Bean
    public Queue queueUpdateNote() {
        return new Queue(QUEUE_UPDATE_NOTE);
    }

    @Bean
    public Queue queueDeleteNote() {
        return new Queue(QUEUE_DELETE_NOTE);
    }

    @Bean
    public Queue queueGetNote() {
        return new Queue(QUEUE_GET_NOTE);
    }

    @Bean
    public Queue queueGetNotesByProject() {
        return new Queue(QUEUE_GET_NOTES_BY_PROJECT);
    }

    @Bean
    public Queue queueGetNotesByTask() {
        return new Queue(QUEUE_GET_NOTES_BY_TASK);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NOTES);
    }

    @Bean
    public Binding bindingCreateNote(Queue queueCreateNote, TopicExchange exchange) {
        return BindingBuilder.bind(queueCreateNote).to(exchange).with(ROUTING_KEY_CREATE_NOTE);
    }

    @Bean
    public Binding bindingUpdateNote(Queue queueUpdateNote, TopicExchange exchange) {
        return BindingBuilder.bind(queueUpdateNote).to(exchange).with(ROUTING_KEY_UPDATE_NOTE);
    }

    @Bean
    public Binding bindingDeleteNote(Queue queueDeleteNote, TopicExchange exchange) {
        return BindingBuilder.bind(queueDeleteNote).to(exchange).with(ROUTING_KEY_DELETE_NOTE);
    }

    @Bean
    public Binding bindingGetNote(Queue queueGetNote, TopicExchange exchange) {
        return BindingBuilder.bind(queueGetNote).to(exchange).with(ROUTING_KEY_GET_NOTE);
    }

    @Bean
    public Binding bindingGetNotesByProject(Queue queueGetNotesByProject, TopicExchange exchange) {
        return BindingBuilder.bind(queueGetNotesByProject).to(exchange).with(ROUTING_KEY_GET_NOTES_BY_PROJECT);
    }

    @Bean
    public Binding bindingGetNotesByTask(Queue queueGetNotesByTask, TopicExchange exchange) {
        return BindingBuilder.bind(queueGetNotesByTask).to(exchange).with(ROUTING_KEY_GET_NOTES_BY_TASK);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.klug.projectmanager.dto", "com.klug.noteservice.dto", "java.util");

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("note", NoteDTO.class);
        typeMapper.setIdClassMapping(idClassMapping);

        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
