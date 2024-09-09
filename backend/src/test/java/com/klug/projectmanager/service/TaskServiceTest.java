package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.TaskDTO;
import com.klug.projectmanager.entity.Task;
import com.klug.projectmanager.entity.TaskHistory;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.TaskHistoryRepository;
import com.klug.projectmanager.repository.TaskRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @Mock
    private UserRepository userRepository;

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
        taskDTO.setAssignedToId(2L);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setName("Original Task");
        existingTask.setDeadline(LocalDate.now().plusDays(7));
        existingTask.setStatus("Not Started");
        existingTask.setDeleted(false);

        User assignedUser = new User();
        assignedUser.setId(2L);
        assignedUser.setUsername("assignedUser");

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignedUser));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(modelMapper.map(existingTask, TaskDTO.class)).thenReturn(taskDTO);

        // Act
        TaskDTO result = taskService.updateTask(taskId, taskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Task", result.getName());
        verify(taskHistoryRepository, times(4)).save(any(TaskHistory.class)); // Name, deadline, status, and assignedTo changes
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_TaskNotFound_ShouldThrowException() {
        // Arrange
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.updateTask(taskId, taskDTO));
        assertEquals("Tarefa não encontrada.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
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

    @Test
    void getTasksByUser_ShouldReturnUserTasks() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Task 2");

        List<Task> userTasks = Arrays.asList(task1, task2);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(taskRepository.findByAssignedTo(user)).thenReturn(userTasks);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenAnswer(invocation -> {
            Task source = invocation.getArgument(0);
            TaskDTO dto = new TaskDTO();
            dto.setId(source.getId());
            dto.setName(source.getName());
            return dto;
        });

        // Act
        List<TaskDTO> result = taskService.getTasksByUser(username);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertEquals("Task 2", result.get(1).getName());
    }

    @Test
    void deleteTask_ShouldSoftDeleteTask() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setName("Task to delete");
        task.setDeleted(false);

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(taskId);

        // Assert
        assertTrue(task.isDeleted());
        verify(taskRepository, times(1)).save(task);
        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));
    }
}
