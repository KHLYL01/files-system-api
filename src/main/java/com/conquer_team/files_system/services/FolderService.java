package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.InvitationUserToGroupRequest;
import com.conquer_team.files_system.model.dto.requests.UpdateFolderRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;

import java.util.List;

public interface FolderService {

    List<FolderResponse> findAll();

    FolderResponse findById(Long id);

//    FolderResponse addUserToFolder(AddUserToFolderRequest request);

    FolderResponse save(AddFolderRequest request);


    FolderResponse update(UpdateFolderRequest request,Long id);

    void deleteById(Long id);

    List<FolderResponse> getMyFolder();

    List<FolderResponse> getOtherFolder();

    void invitationUser(InvitationUserToGroupRequest request);
}
