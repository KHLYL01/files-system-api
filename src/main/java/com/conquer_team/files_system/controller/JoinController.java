package com.conquer_team.files_system.controller;


import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.notation.AdminFolder;
import com.conquer_team.files_system.services.FolderService;
import com.conquer_team.files_system.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/joins")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class JoinController {

    private final FolderService folderService;
    private final UserService userService;

    @GetMapping("/my-invites")
    public ResponseEntity<?> getInvitations() {
        return ResponseEntity.ok(userService.getInvitations());
    }

    @GetMapping("/requests/{folderId}")
    @AdminFolder
    public ResponseEntity<?> getRequestsJoining(@PathVariable long folderId) {
        return ResponseEntity.ok(userService.getRequestsJoining(folderId));
    }

    @PostMapping("/invite-user")
    @AdminFolder
    public ResponseEntity<?> invitationUserToGroup(@RequestBody InvitationUserToGroupRequest request){
        folderService.invitationUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/requests")
    public ResponseEntity<?> joinToGroup(@RequestBody JoinToGroupRequest request) {
        userService.joinToGroup(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptInvitationOrJoinRequest(@RequestParam long id) {
        userService.acceptInvitationOrJoinRequest(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectInvitationOrJoinRequest(@PathVariable long id) {
        userService.rejectInvitationOrJoinRequest(id);
        return ResponseEntity.ok().build();
    }

}
