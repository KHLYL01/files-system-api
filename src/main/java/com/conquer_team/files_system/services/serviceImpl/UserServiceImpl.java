package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.LeaveFolderRequest;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.model.mapper.UserFolderMapper;
import com.conquer_team.files_system.model.mapper.UserMapper;
import com.conquer_team.files_system.repository.*;
import com.conquer_team.files_system.services.OutBoxService;
import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final OutBoxRepo outBoxRepo;
    private final UserMapper mapper;
    private final FolderRepo folderRepo;
    private final JwtService jwtService;
    private final UserFolderRepo userFolderRepo;
    private final UserFolderMapper userFolderMapper;
    private final OutBoxService outBoxService;

    private final FileRepo fileRepo;


    @Override
    public List<UserResponse> getAllOutFolder(long id) {
        List<User> users = repo.findUsersOutFolder(id);
        return mapper.toDtos(users);
    }

    @Override
    public UserResponse findById(long id) {
        User user = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        return mapper.toDto(user);
    }

    @Override
    public List<UserResponse> usersInFolder(long id) {
        return null;
    }

    @Override
    public List<UserResponse> findAll() {
        return mapper.toDtos(repo.findAllByRole(Role.USER));
    }


    @Transactional
    @Override
    public void joinToGroup(JoinToGroupRequest request) {
        // get user
        User user = repo.findByEmail(jwtService.getCurrentUserName()).get();

        // get folder
        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        if(userFolderRepo.existsByUserIdAndFolderIdAndStatus(user.getId(),folder.getId(),JoinStatus.REQUEST)){
           throw new IllegalArgumentException("You have already requested to join.");
        }

        if(userFolderRepo.existsByUserIdAndFolderIdAndStatus(user.getId(),folder.getId(),JoinStatus.ACCEPTED)){
            throw new IllegalArgumentException("You have already in group.");
        }

        // create REQUEST JOIN
        UserFolder userFolder = userFolderMapper.addUserFolder(folder, user, JoinStatus.REQUEST);

        // create notification for storage in outbox to send it later
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("New Join Request for Your Group")
                .message(user.getFullname() + "has requested to join the group [" + folder.getName() + "]. Review and approve or deny the request.")
                .userId(folder.getUser().getId())
                .build();
        // create event to sent notification
        outBoxService.addEvent(notificationRequest, EventTypes.SENT_NOTIFICATION_TO_USER);
        userFolderRepo.save(userFolder);
    }

    @Override
    public List<GetInvitationsResponse> getInvitations() {
        User user = repo.findByEmail(jwtService.getCurrentUserName()).get();
        List<UserFolder> userFolders = userFolderRepo.findByUserIdAndStatus(user.getId(), JoinStatus.INVITATION);
        return userFolderMapper.toDtosInv(userFolders);
    }

    @Override
    public List<GetRequestsJoiningResponse> getRequestsJoining(Long id) {
        List<UserFolder> userFolders = userFolderRepo.findByFolderIdAndStatus(id, JoinStatus.REQUEST);
        return userFolderMapper.toDtosJoin(userFolders);
    }


    @Transactional
    @Override
    public void acceptInvitationOrJoinRequest(long id) {
       UserFolder userFolder = userFolderRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("not found"));
        System.out.println(userFolder.getFolder().getName());
        userFolder.setStatus(JoinStatus.ACCEPTED);
        userFolderRepo.save(userFolder);
        this.checkRepeat(userFolder);
    }

    @Transactional
    @Override
    public void rejectInvitationOrJoinRequest(long id) {
        UserFolder userFolder = userFolderRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("not found"));
        userFolderRepo.delete(userFolder);
        this.checkRepeat(userFolder);
    }

    @Transactional
    @Override
    public void checkRepeat(UserFolder userFolder){
        if(userFolderRepo.existsByUserIdAndFolderIdAndStatus(userFolder.getUser().getId(),userFolder.getFolder().getId(),JoinStatus.REQUEST)){
            UserFolder userFolder1 = userFolderRepo.findByUserIdAndFolderIdAndStatus(userFolder.getUser().getId(),userFolder.getFolder().getId(),JoinStatus.REQUEST);
            userFolderRepo.delete(userFolder1);
        }
        else if(userFolderRepo.existsByUserIdAndFolderIdAndStatus(userFolder.getUser().getId(),userFolder.getFolder().getId(),JoinStatus.INVITATION)){
            UserFolder userFolder1 = userFolderRepo.findByUserIdAndFolderIdAndStatus(userFolder.getUser().getId(),userFolder.getFolder().getId(),JoinStatus.INVITATION);
            System.out.println(userFolder1.getId());
            userFolderRepo.delete(userFolder1);
        }
    }


    @Override
    public void leaveFolder(LeaveFolderRequest request) {

        User user = repo.findById(request.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("user with Id " + request.getUserId() + " not found")
        );

        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder with Id " + request.getFolderId() + " not found")
        );

        if (Objects.equals(user.getId(), folder.getUser().getId())) {
            throw new IllegalArgumentException("user cannot leave, because he is the owner of this folder");
        }

        // check out all file for this user
        List<File> bookedFiles = fileRepo.findAllByBookedUserIdAndFolderId(request.getUserId(), request.getFolderId());

        bookedFiles.forEach(
                file -> {
                    file.setBookedUser(null);
                    file.setStatus(FileStatus.AVAILABLE);
                    // save after check out
                    fileRepo.save(file);
                }
        );

        // create notification for storage in outbox to send it later
        NotificationRequest notificationRequest = request.isLeave()
                ?
                NotificationRequest.builder()
                        .title("Leave Group")
                        .message(user.getFullname() + "has leave group [" + folder.getName() + "].")
                        .userId(folder.getUser().getId())
                        .build()
                :
                NotificationRequest.builder()
                        .title("Removed From Group")
                        .message("You have been removed from group [" + folder.getName() + "].")
                        .userId(user.getId())
                        .build();

        // create event to sent notification
        outBoxService.addEvent(notificationRequest, EventTypes.SENT_NOTIFICATION_TO_USER);

        UserFolder userFolder = userFolderRepo.findByUserIdAndFolderIdAndStatus(request.getUserId(), request.getFolderId(),JoinStatus.ACCEPTED);
        System.out.println(userFolder.getFolder().getName()+"  "+userFolder.getUser().getFullname());
        userFolderRepo.delete(userFolder);

    }
}
