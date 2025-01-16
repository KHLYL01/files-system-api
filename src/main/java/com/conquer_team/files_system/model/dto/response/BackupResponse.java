package com.conquer_team.files_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BackupResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}