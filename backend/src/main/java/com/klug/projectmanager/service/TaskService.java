package com.klug.projectmanager.service;

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
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

        try {
            if (!Objects.equals(task.getName(), taskDTO.getName())) {
                addToHistory(task, "name", task.getName(), taskDTO.getName());
                task.setName(taskDTO.getName());
            }
            if (!Objects.equals(task.getDeadline(), taskDTO.getDeadline())) {
                addToHistory(task, "deadline",
                        task.getDeadline() != null ? task.getDeadline().toString() : "null",
                        taskDTO.getDeadline() != null ? taskDTO.getDeadline().toString() : "null");
                task.setDeadline(taskDTO.getDeadline());
            }
            if (!Objects.equals(task.getStatus(), taskDTO.getStatus())) {
                addToHistory(task, "status", task.getStatus(), taskDTO.getStatus());
                task.setStatus(taskDTO.getStatus());
            }

            if (taskDTO.getAssignedToId() != null &&
                    !Objects.equals(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null, taskDTO.getAssignedToId())) {
                User assignedUser = userRepository.findById(taskDTO.getAssignedToId())
                        .orElseThrow(() -> new CustomException("Usuário atribuído não encontrado.", HttpStatus.NOT_FOUND));
                addToHistory(task, "assignedTo",
                        task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "null",
                        assignedUser.getUsername());
                task.setAssignedTo(assignedUser);
            }

            Task updatedTask = taskRepository.save(task);
            return mapToDTO(updatedTask);
        } catch (OptimisticLockingFailureException e) {
            throw new CustomException("A tarefa foi modificada por outro usuário. Por favor, tente novamente.", HttpStatus.CONFLICT);
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
}