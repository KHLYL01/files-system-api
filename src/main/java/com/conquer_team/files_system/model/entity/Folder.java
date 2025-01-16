package com.conquer_team.files_system.model.entity;

import com.conquer_team.files_system.model.enums.FolderSetting;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(targetClass = FolderSetting.class,fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<FolderSetting> settings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "folder",fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE})
    private List<File> listOfFiles;

    @OneToMany(mappedBy = "folder",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<UserFolder> userFolders;

}
