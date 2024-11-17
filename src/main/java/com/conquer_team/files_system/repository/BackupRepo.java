package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Backups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackupRepo extends JpaRepository<Backups, Long> {
    List<Backups> findAllByFileId(Long fileId);

    List<Backups> findAllByUserIdAndFileId(Long userId,Long fileId);
}
