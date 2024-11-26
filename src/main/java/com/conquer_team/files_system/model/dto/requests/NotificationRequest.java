package com.conquer_team.files_system.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NotificationRequest  {
    private String tittle;
    private String message;
    private String topic;
    private Long user_id;
}
