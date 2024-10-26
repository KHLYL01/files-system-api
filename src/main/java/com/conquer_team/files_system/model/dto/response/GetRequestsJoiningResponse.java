package com.conquer_team.files_system.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestsJoiningResponse {
    private long userId;
    private Long JoinRequestId;
    private String nameUser;
    private LocalDateTime sendAt;
}