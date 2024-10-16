package com.conquer_team.files_system.model.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;
}
