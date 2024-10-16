package com.conquer_team.files_system.model.dto.requests;

import com.conquer_team.files_system.model.enums.FileStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AddUserFileRequest {
    private Long userId;
    private FileStatus status;
    private MultipartFile file;
}
