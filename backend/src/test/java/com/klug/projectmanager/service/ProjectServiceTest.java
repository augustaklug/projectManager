package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.ProjectDTO;
import com.klug.projectmanager.entity.Project;
import com.klug.projectmanager.entity.ProjectHistory;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.ProjectHistoryRepository;
import com.klug.projectmanager.repository.ProjectRepository;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectHistoryRepository projectHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_ShouldCreateProjectAndHistory() {
        // Arrange
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setStartDate(LocalDate.now());
        projectDTO.setEndDate(LocalDate.now().plusDays(30));

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(modelMapper.map(projectDTO, Project.class)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        // Act
        ProjectDTO result = projectService.createProject(projectDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectHistoryRepository, times(1)).save(any(ProjectHistory.class));
    }

    @Test
    void updateProject_ShouldUpdateProjectAndCreateHistory() {
        // Arrange
        Long projectId = 1L;
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Updated Project");
        projectDTO.setStartDate(LocalDate.now());
        projectDTO.setEndDate(LocalDate.now().plusDays(30));

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Original Project");
        existingProject.setStartDate(LocalDate.now().minusDays(1));
        existingProject.setEndDate(LocalDate.now().plusDays(29));
        existingProject.setDeleted(false);  // Adicione esta linha

        when(projectRepository.findByIdAndIsDeletedFalse(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);
        when(modelMapper.map(existingProject, ProjectDTO.class)).thenReturn(projectDTO);

        // Act
        ProjectDTO result = projectService.updateProject(projectId, projectDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        verify(projectHistoryRepository, times(3)).save(any(ProjectHistory.class)); // Name, startDate, and endDate changes
    }

    @Test
    void updateProject_ProjectNotFound_ShouldThrowException() {
        // Arrange
        Long projectId = 1L;
        ProjectDTO projectDTO = new ProjectDTO();
        when(projectRepository.findByIdAndIsDeletedFalse(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> projectService.updateProject(projectId, projectDTO));
        assertEquals("Projeto não encontrado.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getProjectHistory_ShouldReturnHistoryList() {
        // Arrange
        Long projectId = 1L;
        List<ProjectHistory> historyList = Arrays.asList(
                new ProjectHistory(), new ProjectHistory()
        );
        when(projectHistoryRepository.findByProjectIdOrderByChangeDateDesc(projectId)).thenReturn(historyList);

        // Act
        List<ProjectHistory> result = projectService.getProjectHistory(projectId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getProjectsByUser_ShouldReturnUserProjects() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        List<Project> userProjects = Arrays.asList(project1, project2);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(projectRepository.findByTeamMembersContaining(user)).thenReturn(userProjects);
        when(modelMapper.map(any(Project.class), eq(ProjectDTO.class))).thenAnswer(invocation -> {
            Project source = invocation.getArgument(0);
            ProjectDTO dto = new ProjectDTO();
            dto.setId(source.getId());
            dto.setName(source.getName());
            return dto;
        });

        // Act
        List<ProjectDTO> result = projectService.getProjectsByUser(username);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Project 2", result.get(1).getName());
    }

    @Test
    void deleteProject_ShouldSoftDeleteProject() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Project to delete");
        project.setDeleted(false);

        when(projectRepository.findByIdAndIsDeletedFalse(projectId)).thenReturn(Optional.of(project));

        // Act
        projectService.deleteProject(projectId);

        // Assert
        assertTrue(project.isDeleted());
        verify(projectRepository, times(1)).save(project);
        verify(projectHistoryRepository, times(1)).save(any(ProjectHistory.class));
    }
}
