package com.conquer_team.files_system.model.dto.response;

import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.FileStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {

    private Long id;
    private String name;
    private String url;
    private FileStatus status;
    private UserResponse bookedUser;
    private long folderId;

}
