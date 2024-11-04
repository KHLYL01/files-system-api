package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notifications,Long> {
}
