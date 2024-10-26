package com.conquer_team.files_system.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CheckOutRequest {
    private final MultipartFile file;
}
