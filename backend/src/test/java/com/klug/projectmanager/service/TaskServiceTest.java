package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.entity.Task;
import com.klug.projectmanager.entity.TaskHistory;
import com.klug.projectmanager.repository.TaskHistoryRepository;
import com.klug.projectmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldCreateTaskAndHistory() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Test Task");
        taskDTO.setDeadline(LocalDate.now().plusDays(7));

        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");

        when(modelMapper.map(taskDTO, Task.class)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

        // Act
        TaskDTO result = taskService.createTask(taskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Task", result.getName());
        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));
    }

    @Test
    void updateTask_ShouldUpdateTaskAndCreateHistory() {
        // Arrange
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Updated Task");
        taskDTO.setDeadline(LocalDate.now().plusDays(14));
        taskDTO.setStatus("In Progress");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setName("Original Task");
        existingTask.setDeadline(LocalDate.now().plusDays(7));
        existingTask.setStatus("Not Started");
        existingTask.setDeleted(false);  // Adicione esta linha

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(modelMapper.map(existingTask, TaskDTO.class)).thenReturn(taskDTO);

        // Act
        TaskDTO result = taskService.updateTask(taskId, taskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Task", result.getName());
        verify(taskHistoryRepository, times(3)).save(any(TaskHistory.class)); // Name, deadline, and status changes
    }

    @Test
    void getTaskHistory_ShouldReturnHistoryList() {
        // Arrange
        Long taskId = 1L;
        List<TaskHistory> historyList = Arrays.asList(
                new TaskHistory(), new TaskHistory()
        );
        when(taskHistoryRepository.findByTaskIdOrderByChangeDateDesc(taskId)).thenReturn(historyList);

        // Act
        List<TaskHistory> result = taskService.getTaskHistory(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
