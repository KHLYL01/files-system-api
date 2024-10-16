package com.conquer_team.files_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FolderResponse {

    private Long id;
    private String name;
    private List<FileResponse> listOfFiles;
    private List<UserResponse> listOfUsers;

}
