package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.services.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backups")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class BackupController {

    private final BackupService backupService;

    @GetMapping
    public ResponseEntity<?> findAllByParam(@RequestParam Long fileId,@RequestParam(required = false) Long userId)  {
        if(userId != null){
            return ResponseEntity.ok(backupService.findAllByUserIdAndFileId(userId,fileId));
        }
        return ResponseEntity.ok(backupService.findAllByFileId(fileId));
    }

}
