package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.model.mapper.UserMapper;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final UserMapper mapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> repo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("user with email " + username + " is not found")
        );
    }

    @Override
    public List<UserResponse> findAll() {
        return mapper.toDtos(repo.findAllByRole(Role.USER));
    }
}
