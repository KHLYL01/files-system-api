package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> repo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("user with email " + username + " is not found")
        );
    }
}
