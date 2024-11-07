package com.conquer_team.files_system.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFolderRequest {
    private String name;
    private boolean privateFolder;
    private boolean disableAddFile;
}