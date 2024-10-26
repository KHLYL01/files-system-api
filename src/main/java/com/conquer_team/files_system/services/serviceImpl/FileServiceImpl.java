package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.mapper.FileMapper;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.FileService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepo repo;
    private final FileMapper mapper;
    private final JwtService jwtService;
    private final FolderRepo folderRepo;
    private final UserRepo userRepo;
    private final UserFolderRepo userFolderRepo;

    @Value("${image.directory}")
    private String uploadImageDirectory;

    @Override
    public List<FileResponse> findAll() {
        return mapper.toDtos(repo.findAll());
    }

    @Override
    public List<FileResponse> findAllByUserId(Long userId) {
        return mapper.toDtos(repo.findAllByUserId(userId));
    }

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
    public FileResponse save(AddUserFileRequest request) throws IOException {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(
                () -> new IllegalArgumentException("user not found")
        );

        Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(() ->
                new IllegalArgumentException("folder not found"));

        String filename = uploadFile(request.getFile());
        File file = mapper.toEntity(request, filename, user);

        folder.addFiles(file);
        file.setFolder(folder);

        return mapper.toDto(repo.save(file));
    }

    @Override
    public FileResponse checkIn(CheckInFileRequest request) {
        File file = repo.findById(request.getFileId()).orElseThrow(
                () -> new IllegalArgumentException("File with id " + request.getFileId() + " is not found")
        );
        if (file.getStatus() == FileStatus.AVAILABLE) {
            User user = userRepo.findById(request.getUserId()).orElseThrow(
                    () -> new IllegalArgumentException("User with id " + request.getUserId() + " is not found")
            );

            if (user.getId() != file.getFolder().getUser().getId() &&
                    !userFolderRepo.searchByUserIdAndFolderIdAndStatus(user.getId(), file.getFolder().getId(), FileStatus.AVAILABLE).isPresent()) {
                throw new AccessDeniedException("You do not have the necessary permissions to access this resource.");
            }

            file.setStatus(FileStatus.UNAVAILABLE);
            file.setBookedUser(user);
            user.addFile(file);
            return mapper.toDto(repo.save(file));
        } else {
            throw new IllegalArgumentException("File with id " + request.getFileId() + " is not Available");
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

    @Override
    public void checkOutWithoutUpdate(Long fileId) {
        File file = repo.findById(fileId).get();
        if (file.getStatus() == FileStatus.UNAVAILABLE) {
            file.setStatus(FileStatus.AVAILABLE);
            file.setBookedUser(null);
            repo.save(file);
        } else {
            throw new IllegalArgumentException("File with id " + fileId + " is Available");
        }
    }

    @Transactional
    @Override
    public FileResponse checkOutWithUpdate(CheckOutRequest request, long id)throws IOException {
        File file = repo.findById(id).get();
        MultipartFile file1 = request.getFile();
        String fileName = file1.getOriginalFilename();
        if(!file.getName().contains(fileName)){
            throw new IllegalArgumentException("Please upload the same file");
        }
        else{
            System.out.println("AAAAAAAAAAA");
            String fileName2 = uploadFile(request.getFile());
            file.setName(fileName2);
            file.setStatus(FileStatus.AVAILABLE);
            file.setBookedUser(null);
            return mapper.toDto(repo.save(file));
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
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path uploadPath = Path.of(uploadImageDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

//    @Override
//    public byte[] viewFile(String fileName) throws IOException {
//        Path filePath = Path.of(uploadImageDirectory, fileName);
//
//        if (Files.exists(filePath)) {
//            byte[] fileBytes = Files.readAllBytes(filePath);
//            return fileBytes;
//        } else {
//            return null;
//        }
//    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path imagePath = Path.of(uploadImageDirectory, fileName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }


}
