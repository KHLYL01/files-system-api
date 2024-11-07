package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Backups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepo extends JpaRepository<Backups,Long> {}
