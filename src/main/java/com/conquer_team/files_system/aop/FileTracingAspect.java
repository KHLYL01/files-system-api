package com.conquer_team.files_system.aop;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.AddFileRequest;
import com.conquer_team.files_system.model.dto.requests.CheckInFileRequest;
import com.conquer_team.files_system.model.dto.requests.CheckOutRequest;
import com.conquer_team.files_system.model.dto.response.FileResponse;
import com.conquer_team.files_system.model.entity.File;
import com.conquer_team.files_system.model.entity.FileTracing;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.FileOperationType;
import com.conquer_team.files_system.repository.FileRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.FileTracingService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FileTracingAspect {
    private final FileTracingService fileTracingService;
    private final UserRepo userRepo;
    private final FileRepo fileRepo;
    private final JwtService jwtService;

    @AfterReturning(pointcut = "execution(* com.conquer_team.files_system.services.FileService.checkIn(..)) ||" +
            "execution(* com.conquer_team.files_system.services.FileService.checkOut(..))", returning = "result")
    private void addFileTracing(JoinPoint joinPoint, Object result) {
        Object[] objects = joinPoint.getArgs();

        if (objects[0] instanceof CheckInFileRequest request) {

            User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                    new IllegalArgumentException("user not found"));
            File file = fileRepo.findById(request.getFileId()).orElseThrow(
                    () -> new IllegalArgumentException("File with id " + request.getFileId() + " not found")
            );

            fileTracingService.save(FileTracing.builder()
                    .file(file)
                    .user(user)
                    .type(FileOperationType.CHECK_IN)
                    .build());
        }
        if (objects[0] instanceof CheckOutRequest) {

            FileResponse fileResponse = (FileResponse) result;

            User user = userRepo.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                    new IllegalArgumentException("user not found"));

            File file = fileRepo.findById(fileResponse.getId()).orElseThrow(
                    () -> new IllegalArgumentException("File with id " + fileResponse.getId() + " not found")
            );

            fileTracingService.save(FileTracing.builder()
                    .user(user)
                    .file(file)
                    .type(FileOperationType.CHECK_OUT)
                    .build());
        }
    }
}
