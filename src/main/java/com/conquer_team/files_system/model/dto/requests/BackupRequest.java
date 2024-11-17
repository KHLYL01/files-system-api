package com.conquer_team.files_system.model.dto.requests;

import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackupRequest {
    private String name;
    private File file;
    private User user;
}
