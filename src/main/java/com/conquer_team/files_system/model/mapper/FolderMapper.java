package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.UpdateFolderRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;
import com.conquer_team.files_system.model.enums.FolderSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderMapper {

    private final UserMapper userMapper;
    private final FileMapper fileMapper;
    private final UserFolderMapper userFolderMapper;


    public List<FolderResponse> toDtos(List<Folder> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public FolderResponse toDto(Folder entity) {
        if (entity == null) {
            return null;
        }
        return FolderResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(userMapper.toDto(entity.getUser()))
                .files(fileMapper.toDtos(entity.getListOfFiles()))
                .usersInFolder(userFolderMapper.toDtos(entity.getUserFolders()))
                .settings(entity.getSettings())
                .build();
    }


    public Folder toEntity(AddFolderRequest dto) {
        return Folder.builder()
                .name(dto.getName())
                .build();
    }


    public Folder toEntity(UpdateFolderRequest dto,Folder folder) {
        Set<FolderSetting> settings = new HashSet<>();
        if(dto.isPrivateFolder()){
            settings.add(FolderSetting.PRIVATE_FOLDER);
        }
        if(dto.isDisableAddFile()){
            settings.add(FolderSetting.DISABLE_ADD_FILE);
        }

        folder.setName(dto.getName());
        folder.setSettings(settings);

        return folder;
    }
}
