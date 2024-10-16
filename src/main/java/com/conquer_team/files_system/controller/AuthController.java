package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.LoginRequest;
import com.conquer_team.files_system.model.dto.requests.RefreshTokenRequest;
import com.conquer_team.files_system.model.dto.requests.RegisterRequest;
import com.conquer_team.files_system.model.dto.requests.VerificationRequest;
import com.conquer_team.files_system.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


//    @PostMapping("/code")
//    public ResponseEntity<?> reSendCode(@RequestBody ReSendCodeDto dto) {
//        authService.sendCode(dto);
//        return ResponseEntity.ok().build();
//    }

}
