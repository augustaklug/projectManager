package com.klug.projectmanager.service;

import com.klug.projectmanager.messaging.NoteMessageSender;
import com.klug.projectmanager.dto.NoteDTO;
import com.klug.projectmanager.dto.ProjectDTO;
import com.klug.projectmanager.entity.Project;
import com.klug.projectmanager.entity.ProjectHistory;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.ProjectRepository;
import com.klug.projectmanager.repository.ProjectHistoryRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectHistoryRepository projectHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NoteMessageSender noteMessageSender;

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectRepository.existsByNameAndIsDeletedFalse(projectDTO.getName())) {
            throw new CustomException("O nome do projeto já está em uso.", HttpStatus.BAD_REQUEST);
        }

        Project project = mapToEntity(projectDTO);
        project.setDeleted(false);

        List<User> teamMembers = projectDTO.getTeamMemberIds() != null ?
                userRepository.findAllById(List.of(projectDTO.getTeamMemberIds())) :
                new ArrayList<>();
        project.setTeamMembers(teamMembers);

        Project savedProject = projectRepository.save(project);

        addToHistory(savedProject, "Criação", null, "Projeto criado");

        return mapToDTO(savedProject);
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findByIsDeletedFalse();
        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return mapToDTO(project);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND));

        try {
            if (!Objects.equals(project.getName(), projectDTO.getName())) {
                addToHistory(project, "name", project.getName(), projectDTO.getName());
                project.setName(projectDTO.getName());
            }
            if (!Objects.equals(project.getStartDate(), projectDTO.getStartDate())) {
                addToHistory(project, "startDate",
                        project.getStartDate() != null ? project.getStartDate().toString() : "null",
                        projectDTO.getStartDate() != null ? projectDTO.getStartDate().toString() : "null");
                project.setStartDate(projectDTO.getStartDate());
            }
            if (!Objects.equals(project.getEndDate(), projectDTO.getEndDate())) {
                addToHistory(project, "endDate",
                        project.getEndDate() != null ? project.getEndDate().toString() : "null",
                        projectDTO.getEndDate() != null ? projectDTO.getEndDate().toString() : "null");
                project.setEndDate(projectDTO.getEndDate());
            }

            List<User> teamMembers = projectDTO.getTeamMemberIds() != null ?
                    userRepository.findAllById(List.of(projectDTO.getTeamMemberIds())) :
                    new ArrayList<>();
            project.setTeamMembers(teamMembers);

            Project updatedProject = projectRepository.save(project);
            return mapToDTO(updatedProject);
        } catch (OptimisticLockingFailureException e) {
            throw new CustomException("O projeto foi modificado por outro usuário. Por favor, tente novamente.", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND));
        project.setDeleted(true);
        projectRepository.save(project);
        addToHistory(project, "isDeleted", "false", "true");
    }

    private void addToHistory(Project project, String fieldName, String oldValue, String newValue) {
        ProjectHistory history = new ProjectHistory();
        history.setProject(project);
        history.setFieldName(fieldName);
        history.setOldValue(oldValue != null ? oldValue : "null");
        history.setNewValue(newValue != null ? newValue : "null");
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy(getCurrentUsername());
        projectHistoryRepository.save(history);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Sistema";
    }

    private ProjectDTO mapToDTO(Project project) {
        ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
        if (project.getTeamMembers() != null && !project.getTeamMembers().isEmpty()) {
            dto.setTeamMemberIds(project.getTeamMembers().stream()
                    .map(User::getId)
                    .toArray(Long[]::new));
        } else {
            dto.setTeamMemberIds(new Long[0]); // Set empty array instead of null
        }
        return dto;
    }

    private Project mapToEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    public List<ProjectHistory> getProjectHistory(Long projectId) {
        return projectHistoryRepository.findByProjectIdOrderByChangeDateDesc(projectId);
    }

    public List<ProjectDTO> getProjectsByUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<Project> projects = projectRepository.findByTeamMembersContaining(user);
        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> getProjectNotes(Long projectId) {
        return noteMessageSender.getNotesByProjectId(projectId);
    }

    public NoteDTO addNoteToProject(Long projectId, NoteDTO noteDTO) {
        noteDTO.setProjectId(projectId);
        return noteMessageSender.createNote(noteDTO);
    }

    public NoteDTO updateProjectNote(Long projectId, Long noteId, NoteDTO noteDTO) {
        noteDTO.setProjectId(projectId);
        return noteMessageSender.updateNote(noteId, noteDTO);
    }

    public void deleteProjectNote(Long noteId) {
        noteMessageSender.deleteNote(noteId);
    }
}