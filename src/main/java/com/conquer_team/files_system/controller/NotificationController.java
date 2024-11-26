package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping
    public ResponseEntity<?> getAllNotification(){
        return ResponseEntity.status(200).body(notificationService.getAllNotificationsByUserId());
    }
}