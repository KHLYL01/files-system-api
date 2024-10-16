package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.dto.requests.AddFolderRequest;
import com.conquer_team.files_system.model.dto.response.FolderResponse;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.mapper.FolderMapper;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepo repo;
    private final FolderMapper mapper;

    @Override
    public List<FolderResponse> findAll() {
        return mapper.toDtos(repo.findAll());
    }

//    @Override
//    public List<FolderResponse> findAllByUserId(Long userId) {
//        return mapper.toDtos(repo.findAllByUserId(userId));
//    }

    @Override
    public FolderResponse save(AddFolderRequest request) {
        Folder folder = mapper.toEntity(request);
        return mapper.toDto(repo.save(folder));
    }

    @Override
    public void deleteById(Long id) {
        repo.findById(id).ifPresentOrElse(repo::delete, () -> {
            throw new IllegalArgumentException("Folder with id " + id + " is not found");
        });
    }
}
