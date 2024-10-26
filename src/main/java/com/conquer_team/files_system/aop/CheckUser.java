package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.AddUserFileRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
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
public class CheckUser {

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
        if (args[0] instanceof InvitationUserToGroupRequest) {
            InvitationUserToGroupRequest request1 = (InvitationUserToGroupRequest) args[0];
            folderId = request1.getFolderId();
        } else if(args[0] instanceof AddUserFileRequest) {
            AddUserFileRequest request2 = (AddUserFileRequest) args[0];
            folderId = request2.getFolderId();
        }else {
            folderId = (long) args[0];
        }

        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();

        Folder folder = folderRepo.findById(folderId).orElseThrow(() -> new IllegalArgumentException("not found folder"));
        if (user.getId() != folder.getUser().getId()) {
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }


    @Before("execution(* com.conquer_team.files_system.controller.UserController.acceptInvitationOrJoinRequest(..)) ||" +
            "execution(* com.conquer_team.files_system.controller.UserController.rejectInvitationOrJoinRequest(..)) ")
    public void checkUser(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Long userFolderId = (long) args[0];
        System.out.println(userFolderId);

        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
        System.out.print(user.getId());
        UserFolder userFolder = userFolderRepo.findById(userFolderId).get();

        if(user.getId() != userFolder.getUser().getId() && user.getId() != userFolder.getFolder().getUser().getId()){
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }

    @Before("execution(* com.conquer_team.files_system.controller.FileController.checkOutWithoutUpdate(..)) ||" +
            "execution(* com.conquer_team.files_system.controller.FileController.checkOutWithUpdate(..))")
    public void checkBookedUser(JoinPoint joinPoint){
        Long fileId = (long)joinPoint.getArgs()[0];
        File file = fileRepo.findById(fileId).orElseThrow(()->
                new IllegalArgumentException("file not found"));
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
        if(file.getBookedUser().getId() != user.getId()){
            throw new  AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }
    }

//    public void

}
