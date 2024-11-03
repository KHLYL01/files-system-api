package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.UpdateFolderRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.FolderSetting;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.mapper.FolderMapper;
import com.conquer_team.files_system.model.mapper.UserFolderMapper;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepo repo;
    private final FolderMapper mapper;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    private final UserFolderRepo userFolderRepo;
    private final UserFolderMapper userFolderMapper;

    @Override
    public List<FolderResponse> findAll() {
        List<Folder> folders = repo.findAll();
        return mapper.toDtos(folders);
    }

    @Override
    public List<FolderResponse> getMyFolder() {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        return mapper.toDtos(user.getFolders());
    }

    @Override
    public List<FolderResponse> getOtherFolder() {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        return mapper.toDtos(repo.findAllByUserIdNotAndSettingsNotContaining(user.getId(), FolderSetting.PRIVATE_FOLDER));
    }

    @Override
    public FolderResponse findById(Long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("Folder with id " + id + " is not found")
                )
        );
    }

//    @Override
//    public FolderResponse addUserToFolder(AddUserToFolderRequest request) {
//        Folder folder = repo.findById(request.getFolderId()).orElseThrow(
//                () -> new IllegalArgumentException("Folder with id " + request.getFolderId() + " is not found")
//        );
//
//        User user = userRepo.findById(request.getUserId()).orElseThrow(
//                () -> new IllegalArgumentException("User with id " + request.getUserId() + " is not found")
//        );
//        return mapper.toDto(repo.save(folder));
//    }

    @Transactional
    @Override
    public void invitationUser(InvitationUserToGroupRequest request) {

        User user = userRepo.findById(request.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("user by id: " + request.getUserId() + " not found"));
        Folder folder = repo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder by id: " + request.getFolderId() + " not found"));

        UserFolder userFolder = userFolderMapper.addUserFolder(folder, user, JoinStatus.INVITATION);
        userFolderRepo.save(userFolder);
    }


    @Transactional
    @Override
    public FolderResponse save(AddFolderRequest request) {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        Folder folder = mapper.toEntity(request);

        folder.setUser(user);

        return mapper.toDto(repo.save(folder));
    }

    @Override
    public FolderResponse update(UpdateFolderRequest request, Long id) {
        Folder folder = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Folder with id " + id + "not found"));

        folder = mapper.toEntity(request, folder);

        return mapper.toDto(repo.save(folder));
    }

    @Override
    public void deleteById(Long id) {
        repo.findById(id).ifPresentOrElse(repo::delete, () -> {
            throw new IllegalArgumentException("Folder with id " + id + " is not found");
        });
    }
}
