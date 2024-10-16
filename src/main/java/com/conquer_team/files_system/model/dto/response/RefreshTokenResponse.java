package com.conquer_team.files_system.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
}
