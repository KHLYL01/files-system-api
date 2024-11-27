package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<File,Long> {
    List<File> findAllByUserId(Long userId);

    List<File> findAllByFolderId(Long folderId);

    List<File> findAllByBookedUserId(Long userId);

    List<File> findAllByBookedUserIdAndFolderId(Long userId,Long folderId);

}
