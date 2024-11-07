package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.entity.Backups;
import org.springframework.stereotype.Service;

@Service
public class BackupMapper {

    public Backups toEntity(BackupRequest request){
        return Backups.builder()
                .name(request.getName())
                .file(request.getFile())
                .build();
    }

   // public FileResponse
}
