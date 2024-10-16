package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
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


    @PostMapping
    public ResponseEntity<?> save(@RequestBody AddFolderRequest request) {
        return new ResponseEntity<>(folderService.save(request) , HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        folderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
