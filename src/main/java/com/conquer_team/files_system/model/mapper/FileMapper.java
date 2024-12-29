package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.entity.Backups;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileMapper {

    private final UserMapper userMapper;

    public List<FileResponse> toDtos(List<File> entities){
        if(entities == null){
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public FileResponse toDto(File entity){
        if(entity == null){
            return null;
        }
        return FileResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url("http://195.88.87.77:8888/api/v1/downloads/file?filename="+entity.getName()+"/"+entity.getBackups().get(entity.getBackups().size()-1).getName())
                .status(entity.getStatus())
                .bookedUser(userMapper.toDto(entity.getBookedUser()))
                .folderId(entity.getFolder().getId())
                .build();
    }

    public FileResponse toDto(File entity, Backups backups){
        if(entity == null){
            return null;
        }
        return FileResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url("http://195.88.87.77:8888/api/v1/downloads/file?filename="+entity.getName()+"/"+backups.getName())
                .status(entity.getStatus())
                .bookedUser(userMapper.toDto(entity.getBookedUser()))
                .folderId(entity.getFolder().getId())
                .build();
    }

    public File toEntity(String name, User user){
        return  File.builder()
                .name(name)
                .user(user)
                .build();
    }

}
