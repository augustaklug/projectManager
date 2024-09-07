package com.klug.projectmanager.controller;

import com.klug.projectmanager.dto.ProjectDTO;
import com.klug.projectmanager.dto.NoteDTO;
import com.klug.projectmanager.entity.ProjectHistory;
import com.klug.projectmanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ProjectHistory>> getProjectHistory(@PathVariable Long id) {
        List<ProjectHistory> history = projectService.getProjectHistory(id);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ProjectDTO>> getUserProjects() {
        // Obtenha o usuário atual do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        List<ProjectDTO> projects = projectService.getProjectsByUser(currentUsername);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Criar uma nota para um projeto específico
    @PostMapping("/{projectId}/notes")
    public ResponseEntity<NoteDTO> addNoteToProject(@PathVariable Long projectId, @Valid @RequestBody NoteDTO noteDTO) {
        NoteDTO createdNote = projectService.addNoteToProject(projectId, noteDTO);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    // Listar todas as notas para um projeto específico
    @GetMapping("/{projectId}/notes")
    public ResponseEntity<List<NoteDTO>> getNotesForProject(@PathVariable Long projectId) {
        List<NoteDTO> notes = projectService.getProjectNotes(projectId);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    // Atualizar uma nota de um projeto específico
    @PutMapping("/{projectId}/notes/{noteId}")
    public ResponseEntity<NoteDTO> updateProjectNote(@PathVariable Long projectId, @PathVariable Long noteId, @Valid @RequestBody NoteDTO noteDTO) {
        NoteDTO updatedNote = projectService.updateProjectNote(projectId, noteId, noteDTO);
        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
    }

    // Deletar uma nota de um projeto específico
    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<Void> deleteProjectNote(@PathVariable Long noteId) {
        projectService.deleteProjectNote(noteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}