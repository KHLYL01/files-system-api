package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.FileTracing;
import com.conquer_team.files_system.model.enums.FileOperationType;
import com.conquer_team.files_system.model.mapper.FileTracingMapper;
import com.conquer_team.files_system.repository.FileTracingRepo;
import com.conquer_team.files_system.services.FileTracingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileTracingServiceImpl implements FileTracingService {

    private final FileTracingRepo repo;
    private final FileTracingMapper mapper;
    private final FileTracingRepo fileTracingRepo;


    @Override
    public List<FileTracingResponse> findAllByFilter(Long fileId, Long userId, FileOperationType type, LocalDateTime start, LocalDateTime end) {
        return mapper.toDtos(repo.filterFileTracing(fileId,userId,type,start,end));
    }

    @Override
    public FileTracingResponse findById(Long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("File tracing with id: "+id+" not found!")));
    }

    @Override
    public void save(FileTracing object) {
        repo.save(object);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<FileTracingResponse> getTracingOnFileByFileId(long id) {
        Pageable pageable = PageRequest.of(0, 20);
        List<FileTracing> fileTracings = fileTracingRepo.findLatest10ByFileId(id, pageable);
        return mapper.toDtos(fileTracings);
    }

}
