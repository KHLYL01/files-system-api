package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserReportAuthorizationAspect {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final FolderRepo folderRepo;

    // (admin folder) and (Super Admin) only have access
    @Before(value = "execution(* com.conquer_team.files_system.controller.ArchiveController.generateReportToUser(..))")
    public void check(JoinPoint joinPoint) {
        System.out.println("Ameen");
        Object[] args = joinPoint.getArgs();
        long userId = (long) args[1];
        long folderId = (long) args[2];

        User user1 = userRepo.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("user not found :" + userId));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() ->
                new IllegalArgumentException("folder not found :" + folderId));

        User whoSendRequest = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));

        if (!whoSendRequest.getRole().equals(Role.ADMIN) && !(whoSendRequest.getId() == folder.getUser().getId())) {
            throw new IllegalArgumentException("You do not have the necessary permissions to access this resource.");
        }
    }
}
