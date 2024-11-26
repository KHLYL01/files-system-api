package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import com.conquer_team.files_system.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

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
}
