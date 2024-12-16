package com.conquer_team.files_system.model.mapper;


import com.conquer_team.files_system.model.dto.response.FileTracingResponse;
import com.conquer_team.files_system.model.entity.FileTracing;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileTracingMapper {

    public List<FileTracingResponse> toDtos(List<FileTracing> entities){
        if(entities == null){
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public FileTracingResponse toDto(FileTracing entity){
        if(entity == null){
            return null;
        }
        return FileTracingResponse.builder()
                .id(entity.getId())
                .fileId(entity.getFile().getId())
                .userId(entity.getUser().getId())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
