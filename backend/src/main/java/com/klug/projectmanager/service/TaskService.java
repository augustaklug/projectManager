package com.klug.projectmanager.service;

import com.klug.projectmanager.messaging.NoteMessageSender;
import com.klug.projectmanager.dto.NoteDTO;
import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.entity.Task;
import com.klug.projectmanager.entity.TaskHistory;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.TaskRepository;
import com.klug.projectmanager.repository.TaskHistoryRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NoteMessageSender noteMessageSender;

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = mapToEntity(taskDTO);
        task.setDeleted(false);
        Task savedTask = taskRepository.save(task);

        addToHistory(savedTask, "Criação", null, "Tarefa criada");

        return mapToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findByIsDeletedFalse();
        return tasks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));
        return mapToDTO(task);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));

        boolean changed = false;

        if (taskDTO.getName() != null && !taskDTO.getName().equals(task.getName())) {
            addToHistory(task, "name", task.getName(), taskDTO.getName());
            task.setName(taskDTO.getName());
            changed = true;
        }
        if (taskDTO.getStatus() != null && !taskDTO.getStatus().equals(task.getStatus())) {
            addToHistory(task, "status", task.getStatus(), taskDTO.getStatus());
            task.setStatus(taskDTO.getStatus());
            changed = true;
        }
        if (taskDTO.getDeadline() != null && !taskDTO.getDeadline().equals(task.getDeadline())) {
            addToHistory(task, "deadline",
                    task.getDeadline() != null ? task.getDeadline().toString() : "null",
                    taskDTO.getDeadline().toString());
            task.setDeadline(taskDTO.getDeadline());
            changed = true;
        }
        if (taskDTO.getAssignedToId() != null &&
                (task.getAssignedTo() == null || !taskDTO.getAssignedToId().equals(task.getAssignedTo().getId()))) {
            User assignedUser = userRepository.findById(taskDTO.getAssignedToId())
                    .orElseThrow(() -> new CustomException("Usuário atribuído não encontrado.", HttpStatus.NOT_FOUND));
            addToHistory(task, "assignedTo",
                    task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "null",
                    assignedUser.getUsername());
            task.setAssignedTo(assignedUser);
            changed = true;
        }

        if (changed) {
            Task updatedTask = taskRepository.save(task);
            return mapToDTO(updatedTask);
        } else {
            return mapToDTO(task);
        }
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND));
        task.setDeleted(true);
        taskRepository.save(task);
        addToHistory(task, "isDeleted", "false", "true");
    }

    private void addToHistory(Task task, String fieldName, String oldValue, String newValue) {
        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setFieldName(fieldName);
        history.setOldValue(oldValue != null ? oldValue : "null");
        history.setNewValue(newValue != null ? newValue : "null");
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy(getCurrentUsername());
        taskHistoryRepository.save(history);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Sistema";
    }

    private TaskDTO mapToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    private Task mapToEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    public List<TaskHistory> getTaskHistory(Long taskId) {
        return taskHistoryRepository.findByTaskIdOrderByChangeDateDesc(taskId);
    }

    public List<TaskDTO> getTasksByUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<Task> tasks = taskRepository.findByAssignedTo(user);
        return tasks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> getTaskNotes(Long taskId) {
        return noteMessageSender.getNotesByTaskId(taskId);
    }

    public NoteDTO addNoteToTask(Long taskId, NoteDTO noteDTO) {
        noteDTO.setTaskId(taskId);
        return noteMessageSender.createNote(noteDTO);
    }

    public NoteDTO updateTaskNote(Long taskId, Long noteId, NoteDTO noteDTO) {
        noteDTO.setTaskId(taskId);
        return noteMessageSender.updateNote(noteId, noteDTO);
    }

    public void deleteTaskNote(Long noteId) {
        noteMessageSender.deleteNote(noteId);
    }
}