package com.klug.projectmanager.repository;

import com.klug.projectmanager.entity.ProjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory, Long> {
    List<ProjectHistory> findByProjectIdOrderByChangeDateDesc(Long projectId);
}
