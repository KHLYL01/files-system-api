package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.AddFileToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.mapper.FolderMapper;
import com.conquer_team.files_system.model.mapper.JoinMapper;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepo repo;
    private final FolderMapper mapper;
    private final JoinMapper joinMapper;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final FileRepo fileRepo;
    private final UserFolderRepo userFolderRepo;


    @Override
    public List<FolderResponse> findAll() {
            List<Folder> folders = repo.findAll();
            return mapper.toDtos(folders);
//        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
//        System.out.println(user.getFullname());
//        if(user.getFullname().equals("admin")) return mapper.toDtos(repo.findAll());
//        else {
//            List<Folder> folders = repo.findAllByUserId(user.getId());
//            folders.addAll(user.getFolders());
//            return mapper.toDtos(folders);
//        }
    }

//    @Override
//    public List<FolderResponse> getJoinIt() {
//
//    }

    @Override
    public List<FolderResponse> getMyFolder() {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
        List<Folder> folders = user.getFolders();
        return mapper.toDtos(folders);
    }

    @Override
    public FolderResponse findById(Long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("Folder with id " + id + " is not found")
                )
        );
    }

    @Override
    public FolderResponse addUserToFolder(AddUserToFolderRequest request) {
        Folder folder = repo.findById(request.getFolderId()).orElseThrow(
                () -> new IllegalArgumentException("Folder with id " + request.getFolderId() + " is not found")
        );

        User user = userRepo.findById(request.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("User with id " + request.getUserId() + " is not found")
        );

//        if (folder.getListOfUsers() == null) {
//            folder.setListOfUsers(List.of(user));
//        } else {
//            List<User> users = folder.getListOfUsers();
//            users.add(user);
//            folder.setListOfUsers(users);
//        }

        return mapper.toDto(repo.save(folder));
    }

    @Override
    public FolderResponse addFileToFolder(AddFileToFolderRequest request) {
        Folder folder = repo.findById(request.getFolderId()).orElseThrow(
                () -> new IllegalArgumentException("Folder with id " + request.getFolderId() + " is not found")
        );

        File file = fileRepo.findById(request.getFileId()).orElseThrow(
                () -> new IllegalArgumentException("File with id " + request.getFileId() + " is not found")
        );

        if (folder.getListOfFiles() == null) {
            folder.setListOfFiles(List.of(file));
        } else {
            List<File> files = folder.getListOfFiles();
            files.add(file);
            folder.setListOfFiles(files);
        }

        return mapper.toDto(repo.save(folder));
    }

    @Transactional
    @Override
    public void invitationUser(InvitationUserToGroupRequest request) {

        User user = userRepo.findById(request.getUserId()).orElseThrow(()->
                new IllegalArgumentException("user by id:"+" not found"));
        Folder folder = repo.findById(request.getFolderId()).orElseThrow(()->
                new IllegalArgumentException("folder by id:"+"not found"));
        UserFolder userFolder = UserFolder.builder()
                .folder(folder)
                .user(user)
                .type(JoinStatus.INVITATION)
                .build();
        user.addUserFolders(userFolder);
        folder.addUserFolders(userFolder);
    }
//    @Override
//    public List<FolderResponse> findAllByUserId(Long userId) {
//        return mapper.toDtos(repo.findAllByUserId(userId));
//    }


    @Transactional
    @Override
    public FolderResponse save(AddFolderRequest request) {
        System.out.println("?????????????????????????????????????????");
        String userName = jwtService.getCurrentUserName();
        System.out.println(userName);
        User user = userRepo.findByEmail(userName).get();
        Folder folder = mapper.toEntity(request);
        folder.setUser(user);
        user.addFolder(folder);
        return mapper.toDto(repo.save(folder));
    }

    @Override
    public void deleteById(Long id) {
        repo.findById(id).ifPresentOrElse(repo::delete, () -> {
            throw new IllegalArgumentException("Folder with id " + id + " is not found");
        });
    }
}
