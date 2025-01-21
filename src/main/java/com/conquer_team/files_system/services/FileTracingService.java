package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.FileTracing;
import com.conquer_team.files_system.model.enums.FileOperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface FileTracingService {

    List<FileTracingResponse> findAllByFilter(Long fileId,Long userId, FileOperationType type, LocalDateTime start, LocalDateTime end);

    FileTracingResponse findById(Long id);

    void save(FileTracing object);

    void deleteById(Long id);

    List<FileTracingResponse> getTracingOnFileByFileId(long id);


}
