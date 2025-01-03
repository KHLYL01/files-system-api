package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.*;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.enums.FolderSetting;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.mapper.FileMapper;
import com.conquer_team.files_system.repository.*;
import com.conquer_team.files_system.services.BackupService;
import com.conquer_team.files_system.services.FileService;
import com.conquer_team.files_system.services.FileTracingService;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepo repo;
    private final FileMapper mapper;
    private final FolderRepo folderRepo;
    private final UserRepo userRepo;
    private final UserFolderRepo userFolderRepo;
    private final BackupService backupService;
    private final FileTracingService fileTracingService;
    private final OutBoxServiceImpl outBoxService;
    private final ArchiveRepo archiveRepo;
    private final JwtService jwtService;

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

    @Cacheable(value = "files", key = "#userId")
    @Override
    public List<FileResponse> findAllBookedFileByUserId(Long userId) {
        return mapper.toDtos(repo.findAllByBookedUserId(userId));
    }

    @Override
    public List<FileTracingResponse> getTracingOnFileByFileId(long id) {
        return fileTracingService.getTracingOnFileByFileId(id);
    }

    @Cacheable(value = "files", key = "#folderId")
    @Override
    public List<FileResponse> findAllByFolderId(Long folderId) {
        return mapper.toDtos(repo.findAllByFolderIdAndStatusNot(folderId,FileStatus.PENDING));
    }

    @Cacheable(value = "files", key = "#id")
    @Override
    public FileResponse getById(long id) {
        File file = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("file not found by id:" + id));

        return mapper.toDto(file);
    }

    @CacheEvict(value = "files", allEntries = true)
    @Override
    public void acceptOrRejectFile(AcceptOrRejectFileRequest request) {
        File file = repo.findById(request.getFileId()).orElseThrow(() ->
                new IllegalArgumentException("file not found"));
        if (request.isAccept()) {
            file.setStatus(FileStatus.AVAILABLE);
            repo.save(file);
        }
        //repo.delete(file);
        else this.deleteById(request.getFileId());
    }

    @Override
    public List<FileResponse> getPendingFiles(long id) {
        List<File> files = repo.findAllByFolderIdAndStatusIs(id, FileStatus.PENDING);
        return mapper.toDtos(files);
    }

//    @Override
//    public FileResponse acceptFile(long id) {
//        File file = repo.findById(id).orElseThrow(()->
//                new IllegalArgumentException("file not found"));
//        if(!file.getStatus().equals(FileStatus.PENDING)){
//            throw new IllegalArgumentException("the file is accepted.");
//        }
//        file.setStatus(FileStatus.AVAILABLE);
//       return mapper.toDto(repo.save(file));
//    }
//
//    @Override
//    public FileResponse rejectFile(long id) {
//        return null;
//    }

    @CacheEvict(value = "files", allEntries = true)
    @Override
    public void deleteById(Long id) {
        repo.findById(id).ifPresentOrElse(file -> {
            try {
                repo.delete(file);
                deleteFile(file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, () -> {
            throw new IllegalArgumentException("File with id " + id + " is not found");
        });
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

        if (!user.getId().equals(folder.getUser().getId())) {
            file.setStatus(FileStatus.PENDING);
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("New File Uploaded in Your Group")
                    .message(user.getFullname() + " has uploaded a new file to the group [" + folder.getName() + "] . Check it out to review or manage the content.")
                    .userId(folder.getUser().getId())
                    .build();
            //create event to sent notification
            outBoxService.addEvent(notificationRequest, EventTypes.SENT_NOTIFICATION_TO_USER);
        }

        File savedFile = repo.save(file);

        Backups backups = backupService.addBackup(BackupRequest.builder()
                .name(filename)
                .file(savedFile)
                .user(user)
                .build());

        return mapper.toDto(savedFile, backups);
    }

    @CacheEvict(value = "files", allEntries = true)
    @Override
    public FileResponse checkIn(CheckInFileRequest request) {

        Object lock = fileLocks.computeIfAbsent(request.getFileId(), key -> new Object());
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

    @CacheEvict(value = "files", allEntries = true)
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


    @CacheEvict(value = "files", allEntries = true)
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
                    backupService.addBackup(BackupRequest.builder().name(fileName).file(file).user(file.getBookedUser()).build());
                    CompareFilesRequest compareRequest = CompareFilesRequest.builder()
                            .oldFile(file.getName() + "/" + file.getBackups().get(file.getBackups().size() - 2).getName())
                            .newFile(file.getName() + "/" + fileName).userId(userRepo.findByEmail(jwtService.getCurrentUserName()).get().getId())
                            .fileId(file.getId()).build();
                    // create event to compareFiles
                    //2024-12-27_00_23_38482df285-4f1f-4950-bb2a-e99f08439875_New Text Document.txt
                    outBoxService.addEvent(compareRequest, EventTypes.COMPARE_FILES);
                }
                NotificationRequest notificationRequest = NotificationRequest.builder().title("New Update")
                        .message("The file" + fileName + "has been modified by " + file.getBookedUser().getFullname())
                        .folderId(file.getFolder().getId()).build();
                //create event to sent Notification
                outBoxService.addEvent(notificationRequest, EventTypes.SENT_NOTIFICATION_TO_ALL_MEMBERS);
            }
            file.setBookedUser(null);
            return mapper.toDto(repo.save(file));
        } else {
            throw new IllegalArgumentException("File with id " + id + " is UnAvailable");
        }
    }
// 2024-12-26_17_24_196183fe1e-f750-4fe8-8fd2-77807a751494_New Text Document.txt

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
    public void compareFiles(CompareFilesRequest request) {
        String details = "";
        try {
            List<String> oldFileLines = Files.readAllLines(Paths.get(uploadImageDirectory + "/" + request.getOldFile()));
            List<String> newFileLines = Files.readAllLines(Paths.get(uploadImageDirectory + "/" + request.getNewFile()));

            String newFileContent = String.join("\n", newFileLines);

            int newFileSizeInBytes = newFileContent.getBytes("UTF-8").length;

            Patch<String> patch = DiffUtils.diff(oldFileLines, newFileLines);
            String update_type = "No Any Update";

            int size = newFileSizeInBytes, revisedEndLine = 0;
            Set<Integer> changedNewLines = new TreeSet<>();
            if (patch.getDeltas().isEmpty()) {
                details = "No differences found between the files.";
            } else {
                System.out.println("11");
                for (AbstractDelta<String> delta : patch.getDeltas()) {
                    System.out.println("12");
                    update_type = delta.getType().toString();
                    String changeType = delta.getType().toString();
                    int revisedStartLine = delta.getTarget().getPosition() + 1;
                    revisedEndLine = revisedStartLine + delta.getTarget().getLines().size() - 1;
                    for (int i = revisedStartLine; i <= revisedEndLine; i++) {
                        changedNewLines.add(i);
                    }
                    System.out.println("13");
                    if (changeType.equals("CHANGE") || changeType.equals("INSERT") || changeType.equals("DELETE")) {
                        for (int i = revisedStartLine; i <= revisedEndLine; i++) {

                            details += "Line: " + i + " | Change Type: " + changeType + "\n";

                            if (changeType.equals("CHANGE") || changeType.equals("INSERT")) {
                                details += "New content: " + delta.getTarget().getLines().get(i - revisedStartLine) + "\n";
                            }

                            if (changeType.equals("DELETE")) {
                                details += "Old content: " + delta.getSource().getLines().get(i - revisedStartLine) + "\n";
                            }
                        }
                    }
                }
            }

            File file = repo.findById(request.getFileId()).orElseThrow(() ->
                    new IllegalArgumentException("file not found  id: " + request.getFileId()));
            User user = userRepo.findById(request.getUserId()).orElseThrow(() ->
                    new IllegalArgumentException("user not found id: " + request.getUserId()));

            archiveRepo.save(Archive.builder()
                    .size(size)
                    .numOfUpdateLines(changedNewLines.size())
                    .type(update_type)
                    .details(details)
                    .file(file)
                    .user(user)
                    .build());
            System.out.println("Added archive");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

    }


    @Override
    public void deleteFile(String folder) throws IOException {
        Path directoryPath = Path.of(uploadImageDirectory, folder);

        if (Files.exists(directoryPath)) {
            Files.walkFileTree(directoryPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

}