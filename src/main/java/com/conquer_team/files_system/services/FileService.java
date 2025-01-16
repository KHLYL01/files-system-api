package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.File;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileResponse> findAll(int pageNumber,int pageSize);

//    List<FileResponse> findAllByUserId(Long userId);

    List<FileResponse> findAllBookedFileByUserId(Long userId ,int pageNumber,int pageSize);

    void compareFiles(CompareFilesRequest request);

    List<FileResponse> findAllByFolderId(Long folderId,int pageNumber,int pageSize);

    FileResponse save(AddFileRequest request) throws IOException;

    FileResponse checkIn(CheckInFileRequest request);

    List<FileResponse> checkInAll(CheckInAllFileRequest request);

    void deleteById(Long id);

    String uploadFile(MultipartFile file,String path) throws IOException;

    void deleteFile(String fileName) throws IOException;

    FileResponse checkOut(CheckOutRequest request, long id) throws IOException;

    FileResponse getById(long id);

    void acceptOrRejectFile(AcceptOrRejectFileRequest request);

    List<FileResponse> getPendingFiles(long id, int pageNumber,int pageSize);

//    List<FileTracingResponse> getTracingOnFileByFileId(long id);

//    byte[] viewFile(String fileName) throws IOException;





}
