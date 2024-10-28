package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.model.mapper.UserFolderMapper;
import com.conquer_team.files_system.model.mapper.UserMapper;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final UserMapper mapper;
    private final FolderRepo folderRepo;
    private final JwtService jwtService;
    private final UserFolderRepo userFolderRepo;
    private final UserFolderMapper userFolderMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> repo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("user with email " + username + " is not found")
        );
    }

    @Override
    public List<UserResponse> getAllOutFolder(long id) {
        List<User> users = repo.findUsersOutFolder(id);
        return mapper.toDtos(users);
    }

    @Override
    public List<UserResponse> findAll() {
        return mapper.toDtos(repo.findAllByRole(Role.USER));
    }


    @Transactional
    @Override
    public void joinToGroup(JoinToGroupRequest request) {
        User user = repo.findByEmail(jwtService.getCurrentUserName()).get();

        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        UserFolder userFolder = userFolderMapper.addUserFolder(folder,user,JoinStatus.REQUEST);

        //ToDo
//        user.addUserFolders(userFolder);
//        folder.addUserFolders(userFolder);
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
