package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.enums.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFolderRepo extends JpaRepository<UserFolder,Long> {
    List<UserFolder> findByUserIdAndStatus(long id,JoinStatus status);
    List<UserFolder> findByFolderIdAndStatus(long id, JoinStatus status);
   // List<UserFolder> findAllByUserIdAndAndStatus(long id,JoinStatus type);
    Optional<UserFolder> searchByUserIdAndFolderIdAndStatus(long user_id, long folder_id,JoinStatus status);
}
