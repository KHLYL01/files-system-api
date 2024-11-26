package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.enums.EventTypes;

public interface OutBoxService {
    void addEvent(Object obj , EventTypes type);
}
