package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.FileTracing;
import com.conquer_team.files_system.model.enums.FileOperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileTracingRepo extends JpaRepository<FileTracing, Long> {

    @Query(
            value = "SELECT f FROM FileTracing f Where " +
                    "(:fileId IS NULL OR f.file.id = :fileId) AND" +
                    "(:userId IS NULL OR f.user.id = :userId) AND" +
                    "(:type IS NULL OR f.type = :type) AND" +
                    "(:startDate IS NULL OR :endDate IS NULL OR (f.createdAt BETWEEN :startDate AND :endDate))"
    )
    List<FileTracing> filterFileTracing(
            @Param("fileId") Long fileId,
            @Param("userId") Long userId,
            @Param("type") FileOperationType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT ft FROM FileTracing ft WHERE ft.file.id = :fileId ORDER BY ft.createdAt ")
    List<FileTracing> findLatest10ByFileId(@Param("fileId") Long fileId, Pageable pageable);

//    @Query(value = "SELECT * FROM file_tracing WHERE file_id = :fileId ORDER BY timestamp DESC LIMIT 10", nativeQuery = true)
//    List<FileTracing> findLatest10ByFileId(@Param("fileId") Long fileId);

}
