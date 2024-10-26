package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.entity.UserFolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFolderMapper {

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
}
