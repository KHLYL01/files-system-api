package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.mapper.OutBoxMapper;
import com.conquer_team.files_system.repository.OutBoxRepo;
import com.conquer_team.files_system.services.OutBoxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxServiceImpl implements OutBoxService {
    private final OutBoxRepo repo;
    private final ObjectMapper objectMapper;
    private final OutBoxMapper mapper;

    @Override
    public void addEvent(Object obj, EventTypes type) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            repo.save(mapper.toEntity(json, type));
            log.info("Add New Event to OutBox Job {}",type);
        } catch (Exception e) {
            log.error("Error when add new Event",e);
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }
}
