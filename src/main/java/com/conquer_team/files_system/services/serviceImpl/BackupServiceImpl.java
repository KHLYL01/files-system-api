package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.dto.response.BackupResponse;
import com.conquer_team.files_system.model.entity.Backups;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.mapper.BackupMapper;
import com.conquer_team.files_system.repository.BackupRepo;
import com.conquer_team.files_system.services.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final BackupRepo repo;
    private final BackupMapper mapper;
    @Value("${image.directory}")
    private String pathFolder;

    @Cacheable(value = "backups", key = "#fileId")
    @Override
    public List<BackupResponse> findAllByFileId(Long fileId) {
        return mapper.toDtos(repo.findAllByFileId(fileId));
    }

    @Cacheable(value = "backups", key = "#userId +'_'+ #fileId")
    @Override
    public List<BackupResponse> findAllByUserIdAndFileId(Long userId, Long fileId) {
        return mapper.toDtos(repo.findAllByUserIdAndFileId(userId, fileId));
    }

    @CacheEvict(value = "backups", allEntries = true)
    @Override
    public Backups addBackup(BackupRequest request) {
        Backups savedBackup = repo.save(mapper.toEntity(request));

        // check if backups larger than 10
        checkBackup(request.getFile());

        return savedBackup;
    }

    @Transactional
    @Override
    public void deleteBackup(Backups backups) throws IOException {
        Path filePath = Path.of(pathFolder + backups.getFile().getName(), backups.getName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        System.out.println("over 10");
        repo.deleteById(backups.getId());
        System.out.println("delete backup");
    }

    private void checkBackup(File file) {
        List<Backups> backups = file.getBackups();

        if (backups == null) {
            System.out.println("backups is null");
            return;
        }
        if (backups.size() <= 10) {
            System.out.println("backups is " + backups.size());
            return;
        }

        int num = backups.size() - 10;
        for (int i = 0; i < num; i++) {
            try {
                deleteBackup(backups.get(i));
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }

        }
    }
}
