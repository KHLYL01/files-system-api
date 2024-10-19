package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);
}
