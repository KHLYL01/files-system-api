package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserFileRequest;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.dto.response.FolderResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderMapper {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public List<FolderResponse> toDtos(List<Folder> entities){
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public FolderResponse toDto(Folder entity){
        if(entity == null){
            return null;
        }
        return FolderResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .listOfFiles(fileMapper.toDtos(entity.getListOfFiles()))
                .listOfUsers(userMapper.toDtos(entity.getListOfUsers()))
                .build();
    }

    public Folder toEntity(AddFolderRequest dto){
        return  Folder.builder()
                .name(dto.getName())
                .build();
    }


}
