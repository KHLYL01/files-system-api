package com.conquer_team.files_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetInvitationsResponse {
    private long folderId;
    private Long InvitationId;
    private String ownerName;
    private String folderName;
    private LocalDateTime sendAt;
}
