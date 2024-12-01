package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.AddFileRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserAuthorizationAspect {

    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final FolderRepo folderRepo;
    private final UserFolderRepo userFolderRepo;
    private final FileRepo fileRepo;

    //@Before("execution(* com.conquer_team.files_system.controller.FolderController.invitationUserToGroup(..)) && args(request,..)")
    @Before("@annotation(com.conquer_team.files_system.notation.AdminFolder)")
    public void checkAdminFolder(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        long folderId;
        if (args[0] instanceof InvitationUserToGroupRequest request1) {
            folderId = request1.getFolderId();
        } else if (args[0] instanceof AddFileRequest request2) {
            folderId = request2.getFolderId();
        } else {
            folderId = (long) args[0];
        }

        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new IllegalArgumentException("not found folder"));
        if (!user.getId().equals(folder.getUser().getId())) {
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }


    // To verify that the person can accept or reject the claim or request only for him
    @Before(value = "execution(* com.conquer_team.files_system.controller.JoinController.acceptInvitationOrJoinRequest(..)) ||" +
            "execution(* com.conquer_team.files_system.controller.JoinController.rejectInvitationOrJoinRequest(..)) ")
    public void checkUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long userFolderId = (long) args[0];
       // System.out.println(userFolderId);

        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        //System.out.print(user.getId());
        UserFolder userFolder = userFolderRepo.findById(userFolderId).orElseThrow(() ->
                new IllegalArgumentException("user not found"));

        if (!user.getId().equals(userFolder.getUser().getId()) && !user.getId().equals(userFolder.getFolder().getUser().getId())) {
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }

    // check if this user check_in this file
    @Before("execution(* com.conquer_team.files_system.controller.FileController.checkOut(..))")
    public void checkBookedUser(JoinPoint joinPoint) {
        Long fileId = (long) joinPoint.getArgs()[0];
        File file = fileRepo.findById(fileId).orElseThrow(() ->
                new IllegalArgumentException("file not found"));
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        if (!file.getBookedUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }

    @Before("execution(* com.conquer_team.files_system.controller.FileController.findAllByFolderId(..))")
    public void checkUserInFolder(JoinPoint joinPoint){
         Object[] args = joinPoint.getArgs();
        long folderId = (long) args[0];
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(()->
                new IllegalArgumentException("user not found "));
        Folder folder = folderRepo.findById(folderId).orElseThrow(()->
                new IllegalArgumentException("folder not found"));
         if(!userFolderRepo.existsByUserIdAndFolderIdAndStatus(user.getId(),folderId, JoinStatus.ACCEPTED) && !folder.getUser().getId().equals(user.getId()) ){
             throw new IllegalArgumentException("You do not have the necessary permissions to access this resource.");
         }
    }

}
