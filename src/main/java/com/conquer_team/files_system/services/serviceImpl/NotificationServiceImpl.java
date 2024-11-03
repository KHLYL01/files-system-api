package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.conquer_team.files_system.model.mapper.NotificationMapper;
import com.conquer_team.files_system.repository.NotificationRepo;
import com.conquer_team.files_system.services.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final NotificationRepo repo;
    private final NotificationMapper mapper;

    @Transactional
    @Override
    public NotificationResponse sendNotificationToAdminFolder(NotificationRequest request) {
        //save notification info
        repo.save(mapper.toEntity(request));

        //create notification
        Notification notification = Notification.builder()
                .setTitle(request.getTittle())
                .setBody(request.getMessage())
                .build();

        // create message
        Message message = Message.builder()
                .setToken(request.getUser().getFcmToken())
                .setNotification(notification)
                .build();

        //send Notification
        try {
            firebaseMessaging.send(message);
            return NotificationResponse.builder()
                    .message("Sending successfully")
                    .statusCode(200)
                    .build();
        }catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return NotificationResponse.builder()
                    .message("Sending failed")
                    .statusCode(500)
                    .build();
        }
    }
}
