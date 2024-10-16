package com.conquer_team.files_system.model.dto.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckInAllFileRequest {
    private Long userId;
    private List<Long> fileIds;
}
