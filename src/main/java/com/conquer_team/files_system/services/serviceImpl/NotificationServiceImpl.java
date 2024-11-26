package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.config.JwtService;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.conquer_team.files_system.model.entity.Notifications;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.mapper.NotificationMapper;
import com.conquer_team.files_system.repository.NotificationRepo;
import com.conquer_team.files_system.repository.UserFolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final NotificationRepo repo;
    private final UserFolderRepo userFolderRepo;
    private final ObjectMapper objectMapper;
    private final NotificationMapper mapper;
    private final JwtService jwtService;
    private final UserRepo userRepo;


    @Transactional
    @Override
    public void sendNotificationToUser(NotificationRequest request) throws FirebaseMessagingException {
        User user = userRepo.findById(request.getUser_id()).get();

        //create notification
        Notification notification = Notification.builder()
                .setTitle(request.getTittle())
                .setBody(request.getMessage())
                .build();

        // create message
        Message message = generateMessageUsingFcmToken(notification, user.getFcmToken());

        System.out.println(user.getFcmToken());

        //send Notification
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("send notification successfully");
        System.out.println(response);

        //save notification info
        user.AddNotification(repo.save(mapper.toEntity(request)));
    }

    public Message generateMessageUsingFcmToken(Notification notification, String token) {

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        return message;
    }


    @Override
    public void sendNotificationToAllMembers(NotificationRequest request) throws FirebaseMessagingException {

        //create notification
        Notification notification = Notification.builder()
                .setTitle(request.getTittle())
                .setBody(request.getMessage())
                .build();


        //create message
        Message message = generateMessageUsingTopic(notification, request.getTopic());

        //send notification
        String response = FirebaseMessaging.getInstance().send(message);

        System.out.println(response);

        // save notification
        Notifications notificat = repo.save(mapper.toEntity(request));
        saveNotificationToAllMembers(request.getTopic().substring(5), notificat);
    }

    private void saveNotificationToAllMembers(String folderId, Notifications notificat) {
        //get all members in folder then save notification for every one
        List<UserFolder> users = userFolderRepo.findByFolderIdAndStatus(Integer.getInteger(folderId), JoinStatus.ACCEPTED);
        System.out.println("//////////////////////////////////////////");
        System.out.println("number user in this folder");
        System.out.println("//////////////////////////////////////////");
        for (UserFolder user : users) {
            user.getUser().AddNotification(notificat);
        }
    }

    public Message generateMessageUsingTopic(Notification notification, String topic) {
        Message message = Message.builder()
                .setNotification(notification)
                .setTopic("f02gXZqyiLxdSM-R21tfmd:APA91bETMBmujgpgce9FT6rioG09l8O_LWyqVrK_t4s4vEy34T_G0JpCSvW41GmGEnKmzBgY3Ffv0p_-y1FI8NI-YpJkuEgxE-3_cu7t72jHaFfbf6J2Dek")
                .build();
        return message;
    }

    @Override
    public List<NotificationResponse> getAllNotificationsByUserId() {
        User user = userRepo.findByEmail(jwtService.getCurrentUserName()).get();
        List<Notifications> notifications = user.getNotifications();
        return mapper.toDtos(notifications);
    }
}
