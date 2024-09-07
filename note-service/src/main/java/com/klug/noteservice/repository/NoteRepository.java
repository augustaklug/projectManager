package com.klug.noteservice.repository;

import com.klug.noteservice.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTaskId(Long taskId);
    List<Note> findByProjectId(Long projectId);
}