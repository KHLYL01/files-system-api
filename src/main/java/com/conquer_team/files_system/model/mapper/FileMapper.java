package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.dto.requests.AddUserFileRequest;
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
                .url("localhost:8080/api/v1/downloads/"+entity.getName())
                .status(entity.getStatus())
                .bookedUser(userMapper.toDto(entity.getBookedUser()))
                .build();
    }

    public File toEntity(AddUserFileRequest dto, String name, User user){
        return  File.builder()
                .status(dto.getStatus())
                .name(name)
                .user(user)
                .build();
    }

}
