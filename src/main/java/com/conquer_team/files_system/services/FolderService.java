package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.requests.AddUserToFolderRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;

import java.util.List;

public interface FolderService {

    List<FolderResponse> findAll();

//    List<FolderResponse> findAllByUserId(Long userId);
    FolderResponse addUserToFolder(AddUserToFolderRequest request);

    FolderResponse save(AddFolderRequest request);

    void deleteById(Long id);
}
