package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.entity.Task;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.TaskRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public TaskDTO createTask(TaskDTO taskDTO) {
        // Mapear o DTO para uma entidade Task
        Task task = mapToEntity(taskDTO);

        // Salvar a tarefa no banco de dados
        Task savedTask = taskRepository.save(task);

        // Mapear a entidade salva de volta para um DTO
        return mapToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            return mapToDTO(taskOptional.get());
        } else {
            throw new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setName(taskDTO.getName());
            task.setDeadline(taskDTO.getDeadline());
            task.setStatus(taskDTO.getStatus());

            // Atualizar o membro atribuído
            if (taskDTO.getAssignedToId() != null) {
                Optional<User> userOptional = userRepository.findById(taskDTO.getAssignedToId());
                if (userOptional.isPresent()) {
                    task.setAssignedTo(userOptional.get());
                } else {
                    throw new CustomException("Usuário atribuído não encontrado.", HttpStatus.NOT_FOUND);
                }
            }

            Task updatedTask = taskRepository.save(task);
            return mapToDTO(updatedTask);
        } else {
            throw new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new CustomException("Tarefa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    private TaskDTO mapToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    private Task mapToEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }
}
