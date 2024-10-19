package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.LoginResponse;
import com.conquer_team.files_system.model.dto.response.RefreshTokenResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void verificationAccount(VerificationRequest request);

    void sendCode(CodeRequest dto);

    void changePassword(ResetPasswordRequest dto);
}
