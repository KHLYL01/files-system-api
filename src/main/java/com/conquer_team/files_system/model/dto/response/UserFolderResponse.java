package com.conquer_team.files_system.model.dto.response;

import com.conquer_team.files_system.model.enums.JoinStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class UserFolderResponse {

    private long id;
    private JoinStatus status;
    private UserResponse user;
    private LocalDateTime createdAt;

}
