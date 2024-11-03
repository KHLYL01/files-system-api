package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.entity.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationMapper {

    public Notification toEntity(NotificationRequest request){
        return Notification.builder()
                .tittle(request.getTittle())
                .message(request.getMessage())
                .user(request.getUser())
                .build();
    }

}
