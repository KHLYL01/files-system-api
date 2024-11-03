package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.LoginResponse;
import com.conquer_team.files_system.model.dto.response.RefreshTokenResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.mapper.UserMapper;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.AuthService;
import com.conquer_team.files_system.services.SendMailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper mapper;
    private final UserRepo repo;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse register(RegisterRequest request) {
        User user = mapper.toEntity(request,passwordEncoder.encode(request.getPassword()));
        User savedUser = repo.save(user);
        sendMailService.sendMail(savedUser.getEmail(), savedUser.getVerificationCode(), "Verification your account");
        return mapper.toDto(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = repo.findByEmail(request.getEmail()).orElseThrow(
                        () -> new IllegalArgumentException("user with email " + request.getEmail() + " is not found")
                );
        user.setFcmToken(request.getFcmToken());
        repo.save(user);

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

        return mapper.toJwtDto(user,jwt,refreshToken);

    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());
        User user = repo.findByEmail(username).orElseThrow(
                () -> new IllegalArgumentException("user with email " + username + " is not found")
        );
        if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
            String jwt = jwtService.generateToken(user);

            return RefreshTokenResponse.builder()
                    .token(jwt)
                    .refreshToken(request.getRefreshToken())
                    .build();
        }
        return null;
    }

    @Override
    public void verificationAccount(VerificationRequest request) {
        User user = repo.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (user.getVerificationCode().equals(request.getVerificationCode())) {
            user.setEnable(true);
            repo.save(user);
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }

    @Override
    public void sendCode(CodeRequest dto) {
            String verificationCode = RandomStringUtils.randomNumeric(6);

            sendMailService.sendMail(dto.getEmail(), verificationCode, "confirm your account");
            User user = repo.findByEmail(dto.getEmail()).get();
            user.setVerificationCode(verificationCode);

            repo.save(user);
    }

    @Override
    public void changePassword(ResetPasswordRequest dto) {
        User user = repo.findByEmail(dto.getEmail()).get();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        repo.save(user);
    }


}
