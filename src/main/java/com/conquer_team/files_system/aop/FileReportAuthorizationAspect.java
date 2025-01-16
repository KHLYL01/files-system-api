package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FileReportAuthorizationAspect {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final FileRepo fileRepo;
    private final UserFolderRepo userFolderRepo;
    private final FolderRepo folderRepo;

    // (users in this folder) and (Admin Folder) and (SuperAdmin)
    @Before(value = "execution(* com.conquer_team.files_system.controller.ArchiveController.generateReportToFile(..))")
    public void check(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        long fileId = (long) args[1];
        long folderId = (long) args[2];

        File file = fileRepo.findById(fileId).orElseThrow(() ->
                new IllegalArgumentException("file not found :" + fileId));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        User whoSendRequest = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));

        if (!whoSendRequest.getRole().equals(Role.ADMIN) &&
                !(whoSendRequest.getId().equals(folder.getUser().getId())) &&
                !(userFolderRepo.existsByUserIdAndFolderIdAndStatus(whoSendRequest.getId(), folderId, JoinStatus.ACCEPTED))) {
            throw new IllegalArgumentException("You do not have the necessary permissions to access this resource.");
        }
    }
}
