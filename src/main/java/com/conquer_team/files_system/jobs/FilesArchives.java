package com.conquer_team.files_system.jobs;

import com.conquer_team.files_system.model.entity.Backups;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.repository.BackupRepo;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.services.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesArchives {
    private final BackupService service;
    private final FileRepo fileRepo;
    @Value("${image.directory}")
    private String pathFolder;

    @Transactional
    @Scheduled(cron = "*/5 * * * * *")
    public void removeIfFileBackupsUp10()  {
        List<File> files = fileRepo.findAll();
        if (files.isEmpty()) return;
        for (File file : files) {
            List<Backups> backups = file.getBackups();
            if (backups.size() > 10) {
                int num = backups.size() - 10;
                for (int i = 0; i < num; i++) {
                    try {
                        service.deleteBackup(backups.get(i));
                    }catch (IOException e){
                    throw new IllegalArgumentException(e.getLocalizedMessage());
                    }
                }
            }
            System.out.println("complete delete archives of "+file.getName());
        }
    }
}
