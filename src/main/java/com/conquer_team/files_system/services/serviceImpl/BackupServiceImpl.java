package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.entity.Backups;
import com.conquer_team.files_system.model.mapper.BackupMapper;
import com.conquer_team.files_system.repository.BackupRepo;
import com.conquer_team.files_system.services.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final BackupRepo repo;
    private final BackupMapper mapper;
    @Value("${image.directory}")
    private String pathFolder;

    @Override
    public Backups addBackup(BackupRequest request) {
        Backups backups = mapper.toEntity(request);
       return repo.save(backups);
    }

    @Transactional
    @Override
    public void deleteBackup(Backups backups) throws IOException {
        Path filePath = Path.of(pathFolder+backups.getFile().getName(),backups.getName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        repo.delete(backups);
    }
}
