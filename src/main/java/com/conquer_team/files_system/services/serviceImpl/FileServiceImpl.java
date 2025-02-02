package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.FileResponse;
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
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


    // Map to manage locks for each file
    private final ConcurrentHashMap<Long, Object> fileLocks = new ConcurrentHashMap<>();

    @Override
    public Page<FileResponse> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC,"Id");
        Page<File> page = repo.findAll(pageable);
        return page.map(mapper::toDto);
    }



    @Cacheable(value = "files", key = "#userId +'_'+ #pageNumber + '_'+ #pageSize")
    @Override
    public Page findAllBookedFileByUserId(Long userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC,"Id");
        Page<File> page =repo.findAllByBookedUserId(userId, pageable);
        return page.map(mapper::toDto);
    }


    @Cacheable(value = "files", key = "#folderId +'_'+ #pageNumber + '_'+ #pageSize")
    @Override
    public Page<FileResponse> findAllByFolderId(Long folderId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC,"Id");
        Page<File> page = repo.findAllByFolderIdAndStatusNot(folderId, FileStatus.PENDING, pageable);
        return page.map(mapper::toDto);
    }

    @Cacheable(value = "files", key = "#id")
    @Transactional
    @Override
    public FileResponse getById(long id) {
        File file = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("file not found by id:" + id));

        return mapper.toDto(file);
    }

    @CacheEvict(value = "files", allEntries = true)
    @Transactional
    @Override
    public void acceptOrRejectFile(AcceptOrRejectFileRequest request) {
        File file = repo.findById(request.getFileId()).orElseThrow(() ->
                new IllegalArgumentException("file not found"));
        if (request.isAccept()) {
            file.setStatus(FileStatus.AVAILABLE);
            repo.save(file);
        }
        else this.deleteById(request.getFileId());
    }

    @Override
    public Page<FileResponse> getPendingFiles(long id, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "Id");
        Page<File> page = repo.findAllByFolderIdAndStatusIs(id, FileStatus.PENDING, pageable);
        return page.map(mapper::toDto);
    }

    @CacheEvict(value = "files", allEntries = true)
    @Transactional
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

    @CacheEvict(value = "files", allEntries = true)
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
                .name(request.getFile().getOriginalFilename())
                .file(savedFile)
                .user(user)
                .build());

        return mapper.toDto(savedFile, backups);
    }

    @CacheEvict(value = "files", allEntries = true)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public FileResponse checkIn(CheckInFileRequest request) {

        Object lock = fileLocks.computeIfAbsent(request.getFileId(), key -> new Object());

        synchronized (lock) {
            System.out.println(jwtService.getCurrentUserName());
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
                    Backups backups = backupService.addBackup(BackupRequest.builder().name(fileName).file(file).user(file.getBookedUser()).build());

                    CompareFilesRequest compareRequest = CompareFilesRequest.builder()
                            .oldFile(file.getName() + "/" + file.getBackups().get(file.getBackups().size() - 1).getName())
                            .newFile(file.getName() + "/" + fileName).userId(userRepo.findByEmail(jwtService.getCurrentUserName()).get().getId())
                            .fileId(file.getId()).build();
                    // create event to compareFiles
                    outBoxService.addEvent(compareRequest, EventTypes.COMPARE_FILES);
                }
                System.out.println("A2");
                NotificationRequest notificationRequest = NotificationRequest.builder().title("New Update")
                        .message("The file" + fileName + "has been modified by " + file.getBookedUser().getFullname())
                        .folderId(file.getFolder().getId()).build();
                //create event to sent Notification
                outBoxService.addEvent(notificationRequest, EventTypes.SENT_NOTIFICATION_TO_ALL_MEMBERS);
                System.out.println("A3");
            }
            file.setBookedUser(null);
            return mapper.toDto(repo.save(file));
        } else {
            throw new IllegalArgumentException("File with id " + id + " is UnAvailable");
        }
    }


    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH_mm_ss");
        String timestamp = LocalDateTime.now().format(formatter);
        Path uploadPath = null;
        Path filePath;
        String uniqueFileName = null;//=timestamp + file.getOriginalFilename(); //timestamp + UUID.randomUUID() + "_" + file.getOriginalFilename();
        if (path == null) {
            uploadPath = Path.of(uploadImageDirectory + file.getOriginalFilename());
            uniqueFileName = file.getOriginalFilename();
        } else {
            uploadPath = Path.of(uploadImageDirectory + path);
            uniqueFileName = timestamp + file.getOriginalFilename();
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
       // System.out.println("Ameen?");
        String details = "";
        try {
            List<String> oldFileLines = null;
            List<String> newFileLines = null;
            if (request.getOldFile().contains(".pdf")) {
               // System.out.println("a");
                String FileText1 = convertPdfToText(uploadImageDirectory + "/" + request.getOldFile());
                String FileText2 = convertPdfToText(uploadImageDirectory + "/" + request.getNewFile());
                oldFileLines = Arrays.asList(FileText1.split("\n"));
                newFileLines = Arrays.asList(FileText2.split("\n"));
            } else {
              //  System.out.println("b");
                oldFileLines = Files.readAllLines(Paths.get(uploadImageDirectory + "/" + request.getOldFile()));
                newFileLines = Files.readAllLines(Paths.get(uploadImageDirectory + "/" + request.getNewFile()));
            }
            Patch<String> patch = DiffUtils.diff(oldFileLines, newFileLines);
            Path path = Paths.get(uploadImageDirectory + "/" + request.getNewFile());
            double size = Files.size(path);
            int revisedEndLine = 0;
            int changedLine = 0;
            if (patch.getDeltas().isEmpty()) {
                details = "No differences between the files";
            } else {
                for (AbstractDelta<String> delta : patch.getDeltas()) {
                    DeltaType type = delta.getType();
                    List<String> sourceLines = delta.getSource().getLines();
                    List<String> targetLines = delta.getTarget().getLines();

                    int sourcePosition = delta.getSource().getPosition() + 1;
                    int targetPosition = delta.getTarget().getPosition() + 1;
                    int reservationStartLine = targetPosition;

                    String changeType = delta.getType().toString();
                    revisedEndLine = reservationStartLine + delta.getTarget().getLines().size() - 1;
                    for (int i = reservationStartLine; i <= revisedEndLine; i++) {
                        changedLine++;
                    }

                    if (type == DeltaType.DELETE) {
                        details = details + "Deleted Lines (starting from line " + sourcePosition + "): " + sourceLines + "\n";
                    } else if (type == DeltaType.INSERT) {
                        details = details + "Added Lines (starting from line " + targetPosition + "): " + targetLines + "\n";
                    } else if (type == DeltaType.CHANGE) {
                        details = details + "Original Lines (starting from line " + sourcePosition + "): " + sourceLines + "\n" +
                                "Modified To (starting from line " + targetPosition + "): " + targetLines;
                    }
                }
            }

          //  System.out.println("changed line = " + changedLine);


            File file = repo.findById(request.getFileId()).orElseThrow(() ->
                    new IllegalArgumentException("file not found  id: " + request.getFileId()));
            User user = userRepo.findById(request.getUserId()).orElseThrow(() ->
                    new IllegalArgumentException("user not found id: " + request.getUserId()));

            archiveRepo.save(Archive.builder()
                    .size(size)
                    .details(details)
                    .file(file)
                    .user(user)
                    .build());
            System.out.println("Added archive");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

    }


    private String convertPdfToText(String pdfFilePath) throws IOException {
        java.io.File pdfFile = new java.io.File(pdfFilePath);
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
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