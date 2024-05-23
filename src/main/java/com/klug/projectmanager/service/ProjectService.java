package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.ProjectDTO;
import com.klug.projectmanager.entity.Project;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.CustomException;
import com.klug.projectmanager.repository.ProjectRepository;
import com.klug.projectmanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        // Validar se o nome do projeto é único
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new CustomException("O nome do projeto já está em uso.", HttpStatus.BAD_REQUEST);
        }

        // Mapear o DTO para uma entidade Project
        Project project = mapToEntity(projectDTO);

        // Salvar o projeto no banco de dados
        Project savedProject = projectRepository.save(project);

        // Mapear a entidade salva de volta para um DTO
        return mapToDTO(savedProject);
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            return mapToDTO(projectOptional.get());
        } else {
            throw new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setName(projectDTO.getName());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());

            // Atualizar membros da equipe
            List<User> teamMembers = userRepository.findAllById(List.of(projectDTO.getTeamMemberIds()));
            project.setTeamMembers(teamMembers);

            Project updatedProject = projectRepository.save(project);
            return mapToDTO(updatedProject);
        } else {
            throw new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new CustomException("Projeto não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    private ProjectDTO mapToDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

    private Project mapToEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }
}
