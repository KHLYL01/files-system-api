package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.entity.Backups;

import java.io.IOException;

public interface BackupService {

    Backups addBackup(BackupRequest request);

    void deleteBackup(Backups backups) throws IOException;
}
