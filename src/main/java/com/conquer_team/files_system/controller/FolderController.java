package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.AddFileToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.notation.AdminFolder;
import com.conquer_team.files_system.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/folders")
public class FolderController {

    final private FolderService folderService;

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(folderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.findById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFolders(){
        return ResponseEntity.status(200).body(folderService.getMyFolder());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody AddFolderRequest request) {
        return new ResponseEntity<>(folderService.save(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/invitation/")
    @AdminFolder
    public ResponseEntity<?> invitationUserToGroup(@RequestBody InvitationUserToGroupRequest request){
        folderService.invitationUser(request);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/users")
//    public ResponseEntity<?> addUserToFolder(@RequestBody AddUserToFolderRequest request) {
//        return new ResponseEntity<>(folderService.addUserToFolder(request), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/files")
//    public ResponseEntity<?> addFileToFolder(@RequestBody AddFileToFolderRequest request) {
//        return new ResponseEntity<>(folderService.addFileToFolder(request), HttpStatus.CREATED);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        folderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
