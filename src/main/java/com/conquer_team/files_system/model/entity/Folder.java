package com.conquer_team.files_system.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "folder",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<File> listOfFiles;

    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<UserFolder> userFolders;

    public void addUserFolders(UserFolder userFolder){
        if(userFolders == null){
            userFolders = new ArrayList<>();
            userFolders.add(userFolder);
        }else {
            userFolders.add(userFolder);
        }
    }

    public void addFiles(File file){
        if(listOfFiles == null){
            listOfFiles = new ArrayList<>();
            listOfFiles.add(file);
        }else {
            listOfFiles.add(file);
        }
    }
}
