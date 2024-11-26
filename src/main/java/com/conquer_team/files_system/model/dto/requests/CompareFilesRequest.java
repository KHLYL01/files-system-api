package com.conquer_team.files_system.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CompareFilesRequest {
    private String oldFile;
    private String newFile;
    private long userId;
    private long fileId;
}
