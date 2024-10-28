package com.conquer_team.files_system.model.entity;

import com.conquer_team.files_system.model.enums.FileStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "booked_user_id")
    private User bookedUser;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = FileStatus.AVAILABLE;
        }
    }

}
