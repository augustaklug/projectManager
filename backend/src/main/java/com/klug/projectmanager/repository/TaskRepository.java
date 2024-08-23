package com.klug.projectmanager.repository;

import com.klug.projectmanager.entity.Task;
import com.klug.projectmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);

    List<Task> findByIsDeletedFalse();

    Optional<Task> findByIdAndIsDeletedFalse(Long id);

    List<Task> findByAssignedTo(User user);

    List<Task> findByProjectIdAndIsDeletedFalse(Long projectId);
}
