package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.services.ArchiveService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ArchiveController {

    private final ArchiveService service;

    // (admin folder) and (SuperAdmin)
    @GetMapping("/user")
    public void generateReportToUser(HttpServletResponse response, @RequestParam long userId, @RequestParam long folderId) throws IOException {
        service.reportForUser(response,userId,folderId);
    }

    // (users in this folder) and (Admin Folder) and (SuperAdmin)
    @GetMapping("/file")
    public void generateReportToFile(HttpServletResponse response,@RequestParam long fileId,@RequestParam long folderId) throws IOException {
        service.reportForFile(response,fileId,folderId);
    }

    @GetMapping("/all")
    public void generateReportToAll(HttpServletResponse response) throws IOException {
        service.reportForAll(response);
    }
}