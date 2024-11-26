package com.conquer_team.files_system.services;

import com.conquer_team.files_system.model.entity.Archive;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ArchiveService {
    List<Archive> getAllByUserId(long id);
    List<Archive> getAllByFileId(long id);
    void reportForUser(HttpServletResponse response, long id, long folderId) throws IOException;
    void reportForFile(HttpServletResponse response,long id,long folderId) throws IOException;
    void reportForAll(HttpServletResponse response) throws IOException;
}