package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AllReportAuthorizationAspect {

    private final JwtService jwtService;
    private final UserRepo repo;

    @Before(value = "execution(* com.conquer_team.files_system.controller.ArchiveController.generateReportToAll(..))")
    public void check() {
        User user = repo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("You do not have the necessary permissions to access this resource.");
        }
    }
}
