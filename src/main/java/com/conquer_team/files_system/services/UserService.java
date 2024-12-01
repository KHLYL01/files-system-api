package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.LeaveFolderRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.entity.UserFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    void joinToGroup(JoinToGroupRequest request) ;

    List<GetInvitationsResponse> getInvitations();

    List<GetRequestsJoiningResponse> getRequestsJoining(Long id);


    void acceptInvitationOrJoinRequest(long id);

    void rejectInvitationOrJoinRequest(long id);

    List<UserResponse> getAllOutFolder(long id);

    UserResponse findById(long id);

    void checkRepeat(UserFolder userFolder);

    void leaveFolder(LeaveFolderRequest request);
}
