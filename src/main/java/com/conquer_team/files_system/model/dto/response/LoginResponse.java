package com.conquer_team.files_system.model.dto.response;

import com.conquer_team.files_system.model.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long id;
    private String fullname;
    private String email;
    private Role role;
    private String token;
    private String refreshToken;

}
