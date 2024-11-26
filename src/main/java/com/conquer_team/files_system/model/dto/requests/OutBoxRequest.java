package com.conquer_team.files_system.model.dto.requests;

import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutBoxRequest {
    private EventTypes event;
    private String payload;
    private MessageStatus status;
}
