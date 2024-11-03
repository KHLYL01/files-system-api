package com.conquer_team.files_system.model.dto.response;

import com.conquer_team.files_system.model.enums.FolderSetting;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FolderResponse {

    private Long id;
    private String name;
    private UserResponse owner;
    private List<FileResponse> files;
    private List<UserFolderResponse> usersInFolder;
    private Set<FolderSetting> settings;

}
