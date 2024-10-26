package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.AddFileToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserToFolderRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;

import java.util.List;

public interface FolderService {

    List<FolderResponse> findAll();

    FolderResponse findById(Long id);

//    List<FolderResponse> findAllByUserId(Long userId);
    FolderResponse addUserToFolder(AddUserToFolderRequest request);

    FolderResponse addFileToFolder(AddFileToFolderRequest request);

    FolderResponse save(AddFolderRequest request);

    void deleteById(Long id);

//    List<FolderResponse> getJoinIt();

    List<FolderResponse> getMyFolder();

    void invitationUser(InvitationUserToGroupRequest request);
}
