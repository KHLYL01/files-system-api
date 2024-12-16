package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.enums.FileOperationType;
import com.conquer_team.files_system.services.FileTracingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file-tracing")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class FileTracingController {

    final private FileTracingService fileTracingService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Long fileId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) FileOperationType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(fileTracingService.findAllByFilter(fileId, userId, type, start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(fileTracingService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        fileTracingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
