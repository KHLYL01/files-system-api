package com.conquer_team.files_system.model.mapper;

import com.conquer_team.files_system.model.entity.OutBox;
import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutBoxMapper {

    public OutBox toEntity(String request , EventTypes type){
        return OutBox.builder()
                .status(MessageStatus.PENDING)
                .event_type(type)
                .payload(request)
                .build();
    }
}
