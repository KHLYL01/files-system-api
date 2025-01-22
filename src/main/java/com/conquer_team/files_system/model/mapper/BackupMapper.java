package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.requests.BackupRequest;
import com.conquer_team.files_system.model.dto.response.BackupResponse;
import com.conquer_team.files_system.model.entity.Backups;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackupMapper {

    public List<BackupResponse> toDtos(List<Backups> entities){
        if(entities == null){
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public BackupResponse toDto(Backups entity){
        if(entity == null){
            return null;
        }
        return BackupResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url("http://195.88.87.77:8888/api/v1/downloads/file?filename="+entity.getFile().getName()+"/"+entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Backups toEntity(BackupRequest request){
        return Backups.builder()
                .name(request.getName())
                .file(request.getFile())
                .user(request.getUser())
                .build();
    }

}
