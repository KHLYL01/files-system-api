package com.conquer_team.files_system.model.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AddFileRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private Long folderId;
    private MultipartFile file;
}
