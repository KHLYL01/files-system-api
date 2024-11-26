package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Archive;
import org.apache.commons.lang3.arch.Processor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepo extends JpaRepository<Archive,Long> {
    List<Archive> findAllByUserId(Long id);
    List<Archive> findAllByFileId(Long id);
}