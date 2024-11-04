package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.JoinToGroupRequest;
import com.conquer_team.files_system.model.dto.response.GetInvitationsResponse;
import com.conquer_team.files_system.model.dto.response.GetRequestsJoiningResponse;
import com.conquer_team.files_system.model.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    void joinToGroup(JoinToGroupRequest request);

    List<GetInvitationsResponse> getInvitations();

    List<GetRequestsJoiningResponse> getRequestsJoining(Long id);

    void acceptInvitationOrJoinRequest(long id);

    void rejectInvitationOrJoinRequest(long id);

    List<UserResponse> getAllOutFolder(long id);
}
