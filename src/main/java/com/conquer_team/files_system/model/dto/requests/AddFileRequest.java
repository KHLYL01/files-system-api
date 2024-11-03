package com.conquer_team.files_system.model.dto.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AddFileRequest {
    private Long userId;
    private Long folderId;
    private MultipartFile file;
}