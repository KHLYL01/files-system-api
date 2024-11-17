package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.dto.response.BackupResponse;
import com.conquer_team.files_system.model.entity.Backups;

import java.io.IOException;
import java.util.List;

public interface BackupService {

    List<BackupResponse> findAllByFileId(Long fileId);

    List<BackupResponse> findAllByUserIdAndFileId(Long userId,Long fileId);

    Backups addBackup(BackupRequest request);

    void deleteBackup(Backups backups) throws IOException;
}
