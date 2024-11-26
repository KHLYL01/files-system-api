package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.model.mapper.OutBoxMapper;
import com.conquer_team.files_system.model.mapper.UserFolderMapper;
import com.conquer_team.files_system.model.mapper.UserMapper;
import com.conquer_team.files_system.repository.*;
import com.conquer_team.files_system.services.NotificationService;
import com.conquer_team.files_system.services.OutBoxService;
import com.conquer_team.files_system.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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



    @Override
    public List<UserResponse> getAllOutFolder(long id) {
        List<User> users = repo.findUsersOutFolder(id);
        return mapper.toDtos(users);
    }

    @Override
    public UserResponse findById(long id) {
        User user = repo.findById(id).orElseThrow(()->
                new IllegalArgumentException("user not found"));
        return mapper.toDto(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return mapper.toDtos(repo.findAllByRole(Role.USER));
    }


    @Transactional
    @Override
    public void joinToGroup(JoinToGroupRequest request)  {
        // get user
        User user = repo.findByEmail(jwtService.getCurrentUserName()).get();

        // get folder
        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        // create REQUEST JOIN
        UserFolder userFolder = userFolderMapper.addUserFolder(folder,user,JoinStatus.REQUEST);

        // create notification for storage in outbox to send it later
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .tittle("New Join Request for Your Group")
                .message(user.getFullname() + "has requested to join the group [" + folder.getName() + "]. Review and approve or deny the request.")
                .user_id(folder.getUser().getId())
                .build();
        // create event to sent notification
        outBoxService.addEvent(notificationRequest,EventTypes.SENT_NOTIFICATION_TO_USER);
        userFolderRepo.save(userFolder);
    }

    @Override
    public List<GetInvitationsResponse> getInvitations() {
        User user = repo.findByEmail(jwtService.getCurrentUserName()).get();
        List<UserFolder> userFolders = userFolderRepo.findByUserIdAndStatus(user.getId(),JoinStatus.INVITATION);
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
        userFolder.setStatus(JoinStatus.ACCEPTED);
        userFolderRepo.save(userFolder);
    }

    @Override
    public void rejectInvitationOrJoinRequest(long id) {
        UserFolder userFolder = userFolderRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("not found"));
        userFolderRepo.delete(userFolder);
    }
}
