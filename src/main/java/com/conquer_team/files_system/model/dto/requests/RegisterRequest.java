package com.conquer_team.files_system.model.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String fullname;
}
