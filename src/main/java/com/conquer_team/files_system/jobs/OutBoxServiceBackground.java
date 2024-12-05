package com.conquer_team.files_system.jobs;

import com.conquer_team.files_system.model.dto.requests.CompareFilesRequest;
import com.conquer_team.files_system.model.dto.requests.NotificationRequest;
import com.conquer_team.files_system.model.entity.OutBox;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.MessageStatus;
import com.conquer_team.files_system.repository.OutBoxRepo;
import com.conquer_team.files_system.services.FileService;
import com.conquer_team.files_system.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutBoxServiceBackground {
    private final OutBoxRepo outBoxRepo;
    private final NotificationService notificationService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2_000, initialDelay = 5_000)
    public void getAllEvents() {
        List<OutBox> outBoxes = outBoxRepo.findAllByStatusIsNot(Sort.by(Sort.Order.asc("id")), MessageStatus.SENT);
        for (OutBox box : outBoxes) {
            try {
                if (box.getType().equals(EventTypes.SENT_NOTIFICATION_TO_USER)) {
                    notificationService.sendNotificationToUser(objectMapper.readValue(box.getPayload(), NotificationRequest.class));
                } else if (box.getType().equals(EventTypes.COMPARE_FILES)) {
                    fileService.compareFiles(objectMapper.readValue(box.getPayload(), CompareFilesRequest.class));
                }
                box.setStatus(MessageStatus.SENT);
                outBoxRepo.save(box);
            } catch (Exception e) {
                box.setStatus(MessageStatus.FAILED);
                outBoxRepo.save(box);
                System.out.println(e.getLocalizedMessage());
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }
        }
    }
}

