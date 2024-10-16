package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.LoginRequest;
import com.conquer_team.files_system.model.dto.requests.RefreshTokenRequest;
import com.conquer_team.files_system.model.dto.requests.RegisterRequest;
import com.conquer_team.files_system.model.dto.requests.VerificationRequest;
import com.conquer_team.files_system.model.dto.response.LoginResponse;
import com.conquer_team.files_system.model.dto.response.RefreshTokenResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void verificationAccount(VerificationRequest request);

//    void sendCode(ReSendCodeDto dto);
}
