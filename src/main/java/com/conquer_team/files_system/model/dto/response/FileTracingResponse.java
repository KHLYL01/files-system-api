package com.conquer_team.files_system.model.dto.response;

import com.conquer_team.files_system.model.enums.FileOperationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileTracingResponse {

    private Long id;
    private Long fileId;
    private Long userId;
    private String userName;
    private FileOperationType type;
    private LocalDateTime createdAt;

}
