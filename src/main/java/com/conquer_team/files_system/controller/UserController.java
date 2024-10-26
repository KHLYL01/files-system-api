package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.notation.AdminFolder;
import com.conquer_team.files_system.services.UserService;
import com.conquer_team.files_system.validation.IdExists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/api/v1/users")
public class UserController {

    final private UserService userService;

    @GetMapping("/out-folder/{id}")
    public ResponseEntity<?> getAllUsersOutFolder(@PathVariable long id){
        return ResponseEntity.status(200).body(userService.getAllOutFolder(id));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinToGroup(@RequestBody JoinToGroupRequest request) {
        userService.joinToGroup(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invitations")
    public ResponseEntity<?> getInvitations() {
        return ResponseEntity.status(200).body(userService.getInvitations());
    }

    @GetMapping("/joins/{folder_id}")
    @AdminFolder
    public ResponseEntity<?> getRequestsJoining(@PathVariable long folder_id) {
        return ResponseEntity.status(200).body(userService.getRequestsJoining(folder_id));
    }

    @GetMapping
    ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptInvitationOrJoinRequest(@PathVariable long id) {
        userService.acceptInvitationOrJoinRequest(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectInvitationOrJoinRequest(@PathVariable long id) {
        userService.rejectInvitationOrJoinRequest(id);
        return ResponseEntity.noContent().build();
    }

}
