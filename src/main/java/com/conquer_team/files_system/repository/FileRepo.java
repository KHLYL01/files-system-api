package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.enums.FileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<File,Long> {

    List<File> findAllByUserId(Long userId);

    List<File> findAllByFolderIdAndStatusNot(Long folderId,FileStatus status,Pageable pageable);

    List<File> findAllByBookedUserId(Long userId,Pageable pageable);

    List<File> findAllByBookedUserIdAndFolderId(Long userId,Long folderId);

    List<File> findAllByFolderIdAndStatusIs(Long id,FileStatus status,Pageable pageable);

}
