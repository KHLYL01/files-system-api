package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.conquer_team.files_system.model.entity.Notifications;
import com.conquer_team.files_system.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationMapper {

    private final UserRepo userRepo;
    public Notifications toEntity(NotificationRequest request){
        User user = userRepo.findById(request.getUser_id()).get();
        return Notifications.builder()
                .tittle(request.getTittle())
                .message(request.getMessage())
                .user(user)
                .build();
    }

    public NotificationResponse toDto(Notifications notification){
        return NotificationResponse.builder()
                .tittle(notification.getTittle())
                .message(notification.getMessage())
                .build();
    }

    public List<NotificationResponse> toDtos(List<Notifications> notifications){
        return  notifications.stream().map(this::toDto).collect(Collectors.toList());
    }

}
