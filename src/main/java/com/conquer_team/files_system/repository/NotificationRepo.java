package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
}
