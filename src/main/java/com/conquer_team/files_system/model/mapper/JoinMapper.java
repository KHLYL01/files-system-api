package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.entity.UserFolder;
import org.springframework.stereotype.Service;

@Service
public class JoinMapper {

    public UserFolder toEntity(InvitationUserToGroupRequest request){
        return UserFolder.builder()
                .build();
    }

//    public JoinResponse toDto(UserFolder userFolder) {
//        return JoinResponse.builder()
//                .id(userFolder.getId())
//                .userId(userFolder.getUser().getId())
//                .folderId(userFolder.getFolder().getId())
//                .status(userFolder.getStatus())
//                .build();
//    }
}
