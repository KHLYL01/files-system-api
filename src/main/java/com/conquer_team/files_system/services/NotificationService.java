package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;

import java.util.List;

public interface NotificationService {

   void sendNotificationToUser(NotificationRequest request) throws Exception;
   //Notification createNotification(No)

   //void sendNotificationToAllMembers(NotificationRequest request) throws FirebaseMessagingException;

   List<NotificationResponse> getAllNotificationsByUserId();


}
