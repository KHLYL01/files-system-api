package com.conquer_team.files_system.model.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany
    @JoinTable(
            name = "folder_users",
            joinColumns = @JoinColumn(name = "folder_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> listOfUsers;
}
