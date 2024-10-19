package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    final private UserService userService;

    @GetMapping
    ResponseEntity<?> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

}
