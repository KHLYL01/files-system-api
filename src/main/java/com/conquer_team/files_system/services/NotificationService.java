package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.dto.response.NotificationResponse;

public interface NotificationService {

   NotificationResponse sendNotificationToAdminFolder(NotificationRequest request);
}
