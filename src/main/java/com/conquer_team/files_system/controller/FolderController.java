package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
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
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class FolderController {

    final private FolderService folderService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(folderService.findAll());
    }

    @GetMapping("/my-folders")
    public ResponseEntity<?> getMyFolders(){
        return ResponseEntity.ok(folderService.getMyFolder());
    }

    @GetMapping("/other-folders")
    public ResponseEntity<?> getOtherFolder(){
        return ResponseEntity.ok(folderService.getOtherFolder());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.findById(id));
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody AddFolderRequest request) {
        return new ResponseEntity<>(folderService.save(request), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        folderService.deleteById(id);
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

}
