package com.conquer_team.files_system.model.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckInFileRequest {
    private Long userId;
    private Long fileId;
}
