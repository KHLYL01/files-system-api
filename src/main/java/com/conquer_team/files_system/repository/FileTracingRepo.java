package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.FileTracing;
import com.conquer_team.files_system.model.enums.FileOperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
