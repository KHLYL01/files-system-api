package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.entity.Backups;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.enums.FolderSetting;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.mapper.FileMapper;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.BackupService;
import com.conquer_team.files_system.services.FileService;
import com.conquer_team.files_system.services.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepo repo;
    private final FileMapper mapper;
    private final FolderRepo folderRepo;
    private final UserRepo userRepo;
    private final UserFolderRepo userFolderRepo;
    private final NotificationService notificationService;
    private final BackupService backupService;

    @Value("${image.directory}")
    private String uploadImageDirectory;

//    private final ReentrantLock lock = new ReentrantLock();

    // Map to manage locks for each file
    private final ConcurrentHashMap<Long, Object> fileLocks = new ConcurrentHashMap<>();

    @Override
    public List<FileResponse> findAll() {
        return mapper.toDtos(repo.findAll());
    }

//    @Override
//    public List<FileResponse> findAllByUserId(Long userId) {
//        return mapper.toDtos(repo.findAllByUserId(userId));
//    }

    @Override
    public List<FileResponse> findAllBookedFileByUserId(Long userId) {
        return mapper.toDtos(repo.findAllByBookedUserId(userId));
    }

    @Override
    public List<FileResponse> findAllByFolderId(Long folderId) {
        return mapper.toDtos(repo.findAllByFolderId(folderId));
    }


    @Transactional
    @Override
    public FileResponse save(AddFileRequest request) throws IOException {

        // get folder
        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        // check if user  in folder
        if (!userFolderRepo.existsByUserIdAndFolderIdAndStatus(request.getUserId(), request.getFolderId(), JoinStatus.ACCEPTED) && !folder.getUser().getId().equals(request.getUserId())) {
            System.out.println("you are out this folder");
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }

        // check if user id equals owner id and folder setting not (contains Disable add folder)
        if (!request.getUserId().equals(folder.getUser().getId()) && folder.getSettings().contains(FolderSetting.DISABLE_ADD_FILE)) {
            throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
        }

        User user = userRepo.findById(request.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("user with id: " + request.getUserId() + " not found"));

        String filename = uploadFile(request.getFile(), null);
        File file = mapper.toEntity(filename, user);
        file.setFolder(folder);

        //  send notification to admin folder
        if (!user.getId().equals(folder.getUser().getId())) {
            try {
                notificationService.sendNotificationToUser(
                        NotificationRequest.builder()
                                .tittle("New File Uploaded in Your Group")
                                .message(user.getFullname() + " has uploaded a new file to the group [" + folder.getName() + "] . Check it out to review or manage the content.")
                                .user(folder.getUser())
                                .build());
            } catch (FirebaseMessagingException e) {
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }

        }

        File savedFile = repo.save(file);

        Backups backups = backupService.addBackup(BackupRequest.builder()
                .name(filename)
                .file(savedFile)
                .user(user)
                .build());

        return mapper.toDto(savedFile, backups);
    }

    @Override
    public FileResponse checkIn(CheckInFileRequest request) {

        Object lock  = fileLocks.computeIfAbsent(request.getFileId(),key -> new Object());
//        lock.lock();
        synchronized (lock) {
            try {
                File file = repo.findById(request.getFileId()).orElseThrow(
                        () -> new IllegalArgumentException("File with id " + request.getFileId() + " is not found")
                );
                if (file.getStatus() == FileStatus.AVAILABLE) {
                    User user = userRepo.findById(request.getUserId()).orElseThrow(
                            () -> new IllegalArgumentException("User with id " + request.getUserId() + " is not found")
                    );

                    if (!user.getId().equals(file.getFolder().getUser().getId()) &&
                            !userFolderRepo.existsByUserIdAndFolderIdAndStatus(user.getId(), file.getFolder().getId(), JoinStatus.ACCEPTED)) {
                        throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
                    }

                    file.setStatus(FileStatus.UNAVAILABLE);
                    file.setBookedUser(user);

                    return mapper.toDto(repo.save(file));

                } else {
                    throw new IllegalArgumentException("File with id " + request.getFileId() + " is not Available");
                }
            } finally {
                fileLocks.remove(request.getFileId(), lock);
//                lock.unlock();
            }
        }
    }

    @Transactional
    @Override
    public List<FileResponse> checkInAll(CheckInAllFileRequest request) {
        List<FileResponse> checkInResponse = new ArrayList<>();

        request.getFileIds().forEach(id ->
                checkInResponse.add(checkIn(CheckInFileRequest.builder()
                        .fileId(id)
                        .userId(request.getUserId())
                        .build()))
        );

        return checkInResponse;
    }


    @Transactional
    @Override
    public FileResponse checkOut(CheckOutRequest request, long id) throws IOException {
        File file = repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("File with id " + id + " is not found")
        );
        if (file.getStatus() == FileStatus.UNAVAILABLE) {
            file.setStatus(FileStatus.AVAILABLE);

            if (request.getFile() != null) {
                String fileName = request.getFile().getOriginalFilename();
                if (!file.getName().contains(fileName)) {
                    throw new IllegalArgumentException("Please upload the same file");
                } else {
                    fileName = uploadFile(request.getFile(), file.getName());
                    backupService.addBackup(BackupRequest.builder()
                            .name(fileName)
                            .file(file)
                            .user(file.getBookedUser())
                            .build());
                }

                try {
                    notificationService.sendNotificationToAllMembers(
                            NotificationRequest.builder()
                                    .tittle("New Update")
                                    .message("The file" + fileName + "has been modified by " + file.getBookedUser().getFullname())
                                    .topic("group" + file.getFolder().getId())
                                    .build()
                    );
                } catch (FirebaseMessagingException e) {
                    throw new IllegalArgumentException(e.getLocalizedMessage());
                }

            }
            file.setBookedUser(null);
            return mapper.toDto(repo.save(file));
        } else {
            throw new IllegalArgumentException("File with id " + id + " is Available");
        }

    }

    @Override
    public void deleteById(Long id) {
        repo.findById(id).ifPresentOrElse(file -> {
            if (file.getStatus() == FileStatus.AVAILABLE) {
                try {
                    deleteFile(file.getName());
                    repo.delete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("File with id " + id + " is not Available");
            }
        }, () -> {
            throw new IllegalArgumentException("File with id " + id + " is not found");
        });
    }


    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
        String timestamp = LocalDateTime.now().format(formatter);
        Path uploadPath = null;
        Path filePath;
        String uniqueFileName = timestamp + UUID.randomUUID() + "_" + file.getOriginalFilename();
        if (path == null) {
            uploadPath = Path.of(uploadImageDirectory + uniqueFileName);
        } else {
            uploadPath = Path.of(uploadImageDirectory + path);
        }
        filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path imagePath = Path.of(uploadImageDirectory, fileName);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }

    @Override
    public FileResponse getById(long id) {
        File file = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("file not found by id:" + id));
        return mapper.toDto(file);
    }
}
