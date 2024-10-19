package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    List<UserResponse> findAll();
}
