package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserFolderResponse;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFolderMapper {

    private final UserMapper userMapper;

    public List<UserFolderResponse> toDtos(List<UserFolder> entities){
        if(entities == null){
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserFolderResponse toDto(UserFolder entity){
        if(entity == null){
            return null;
        }
        return UserFolderResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .user(userMapper.toDto(entity.getUser()))
                .createdAt(entity.getCreatedAt())
                .build();
    }


    public GetInvitationsResponse toDtoInv(UserFolder userFolder){
        return GetInvitationsResponse.builder()
                .folderId(userFolder.getFolder().getId())
                .folderName(userFolder.getFolder().getName())
                .ownerName(userFolder.getFolder().getUser().getFullname())
                .sendAt(userFolder.getCreatedAt())
                .InvitationId(userFolder.getId())
                .build();
    }

    public List<GetInvitationsResponse> toDtosInv(List<UserFolder> userFolders){
        return userFolders.stream().map(this::toDtoInv).collect(Collectors.toList());
    }

    public GetRequestsJoiningResponse toDtoJoin(UserFolder userFolder){
        return GetRequestsJoiningResponse.builder()
                .userId(userFolder.getUser().getId())
                .nameUser(userFolder.getUser().getFullname())
                .sendAt(userFolder.getCreatedAt())
                .JoinRequestId(userFolder.getId())
                .build();
    }

    public List<GetRequestsJoiningResponse> toDtosJoin(List<UserFolder> userFolders){
        return userFolders.stream().map(this::toDtoJoin).collect(Collectors.toList());
    }

    public UserFolder addUserFolder(Folder folder, User user, JoinStatus status) {
        return UserFolder.builder()
                .folder(folder)
                .user(user)
                .status(status)
                .build();
    }
}
