package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.OutBox;
import com.conquer_team.files_system.model.enums.MessageStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepo extends JpaRepository<OutBox,Long> {
    List<OutBox> findAllByStatusIsNot(Sort sort, MessageStatus status);
}
