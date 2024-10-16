package com.conquer_team.files_system.model.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddFileToFolderRequest {
    private Long folderId;
    private Long fileId;
}
