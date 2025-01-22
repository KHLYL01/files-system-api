package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    final private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verificationAccount(@RequestBody VerificationRequest request) {
        authService.verificationAccount(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(CodeRequest dto){
        authService.sendCode(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(ResetPasswordRequest dto) {
        authService.changePassword(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> logout(){
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
