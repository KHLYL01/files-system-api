package com.conquer_team.files_system.jobs;

import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.OutBox;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.NotificationRepo;
import com.conquer_team.files_system.repository.OutBoxRepo;
import com.conquer_team.files_system.services.OutBoxService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateNotificationToAllUsers {
    private final FolderRepo folderRepo;
    private NotificationRepo notificationRepo;
    private final OutBoxRepo outBoxRepo;
    private final ObjectMapper objectMapper;
    private final OutBoxService outBoxService;

    @Scheduled(fixedRate = 1_000, initialDelay = 5_000)
    public void saveNotification() throws JsonProcessingException {
        List<OutBox> outBoxes = outBoxRepo.findAllByType(Sort.by(Sort.Order.asc("id")), EventTypes.SENT_NOTIFICATION_TO_ALL_MEMBERS);
        for (OutBox out: outBoxes) {
            NotificationRequest request = objectMapper.readValue(out.getPayload(),NotificationRequest.class);
            Folder folder = folderRepo.findById(request.getFolderId()).orElseThrow(()->
                    new IllegalArgumentException("folder not found"));
            folder.getUserFolders().forEach(userFolder -> {
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .userId(userFolder.getUser().getId())
                        .build();
                outBoxService.addEvent(notificationRequest,EventTypes.SENT_NOTIFICATION_TO_USER);
            });
            outBoxRepo.delete(out);
        }
    }
}
