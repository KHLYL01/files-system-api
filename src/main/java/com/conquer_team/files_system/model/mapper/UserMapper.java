package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.dto.requests.RegisterRequest;
import com.conquer_team.files_system.model.dto.response.LoginResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> toDtos(List<User> entities){
        if (entities == null){
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserResponse toDto(User entity) {
        if(entity == null){
            return null;
        }
        return UserResponse.builder()
                .id(entity.getId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }

    public User toEntity(RegisterRequest dto){
        return User.builder()
                .fullname(dto.getFullname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .enable(false)
                .verificationCode(RandomStringUtils.randomNumeric(6))
                .listOfFiles(null)
                .build();
    }

    public LoginResponse toJwtDto(User entity, String jwt, String refreshToken) {
        return LoginResponse.builder()
                .id(entity.getId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .role(entity.getRole())
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }
}
