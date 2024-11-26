package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFolderRepo extends JpaRepository<UserFolder,Long> {
    List<UserFolder> findByUserIdAndStatus(long id,JoinStatus status);
    List<UserFolder> findByFolderIdAndStatus(long id, JoinStatus status);
   // List<UserFolder> findAllByUserIdAndAndStatus(long id,JoinStatus type);

    boolean existsByUserIdAndFolderIdAndStatus(Long userId,Long folderId, JoinStatus accepted);
}
