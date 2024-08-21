package com.klug.projectmanager.repository;

import com.klug.projectmanager.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserIdOrderByChangeDateDesc(Long userId);
}
