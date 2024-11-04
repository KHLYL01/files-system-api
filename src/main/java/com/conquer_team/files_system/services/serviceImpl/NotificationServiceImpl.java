package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.conquer_team.files_system.model.entity.Notifications;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.mapper.NotificationMapper;
import com.conquer_team.files_system.repository.NotificationRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final NotificationRepo repo;
    private final NotificationMapper mapper;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    @Transactional
    @Override
    public void sendNotificationToAdminFolder(NotificationRequest request) throws FirebaseMessagingException {
        //save notification info
        request.getUser().AddNotification(repo.save(mapper.toEntity(request)));

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
        firebaseMessaging.send(message);
    }

    @Override
    public void sendToAllMembers(NotificationRequest request) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setTopic(request.getTopic())
                .putData("title", request.getTittle())
                .putData("body", request.getMessage())
                .build();
        firebaseMessaging.send(message);

    }

    @Override
    public List<NotificationResponse> getAllNotificationsByUserId() {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
        List<Notifications> notifications = user.getNotifications();
        return mapper.toDtos(notifications);
    }
}
