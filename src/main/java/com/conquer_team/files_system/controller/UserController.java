package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class UserController {

    final private UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/out-folder/{folderId}")
    public ResponseEntity<?> getAllUsersOutFolder(@PathVariable long folderId){
        return ResponseEntity.ok(userService.getAllOutFolder(folderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(userService.findById(id));
    }

}
