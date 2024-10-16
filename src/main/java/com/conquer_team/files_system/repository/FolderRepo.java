package com.conquer_team.files_system.repository;

import com.conquer_team.files_system.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepo extends JpaRepository<Folder,Long> {

}
