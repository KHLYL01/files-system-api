package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.conquer_team.files_system.model.entity.Notifications;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationMapper {

    public Notifications toEntity(NotificationRequest request){
        return Notifications.builder()
                .tittle(request.getTittle())
                .message(request.getMessage())
                .user(request.getUser())
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
