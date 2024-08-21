package com.klug.projectmanager.repository;

import com.klug.projectmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);

    List<Project> findByIsDeletedFalse();

    boolean existsByNameAndIsDeletedFalse(String name);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);
}
