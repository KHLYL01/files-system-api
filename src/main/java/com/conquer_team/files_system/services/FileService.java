package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.AddFileToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserFileRequest;
import com.conquer_team.files_system.model.dto.requests.CheckInAllFileRequest;
import com.conquer_team.files_system.model.dto.requests.CheckInFileRequest;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileResponse> findAll();

    List<FileResponse> findAllByUserId(Long userId);

    List<FileResponse> findAllBookedFileByUserId(Long userId);

    List<FileResponse> findAllByFolderId(Long folderId);

    FileResponse save(AddUserFileRequest request) throws IOException;

    FileResponse addFileToFolder(AddFileToFolderRequest request);

    FileResponse checkIn(CheckInFileRequest request);

    List<FileResponse> checkInAll(CheckInAllFileRequest request);

    void checkOut(Long fileId);

    void deleteById(Long id);

    String uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String fileName) throws IOException;

//    byte[] viewFile(String fileName) throws IOException;





}
