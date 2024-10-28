package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);


//    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT uf.user.id FROM UserFolder uf WHERE uf.folder.id = :folderId)")
    @Query("SELECT u FROM User u WHERE u.id <> (SELECT f.user.id FROM Folder f WHERE f.id = :folderId)" +
            "AND u.id NOT IN (SELECT uf.user.id FROM UserFolder uf WHERE uf.folder.id = :folderId)")
    List<User> findUsersOutFolder(@Param("folderId") Long folderId);


}
