package com.conquer_team.files_system.model.entity;

import com.conquer_team.files_system.model.enums.FileStatus;
import com.conquer_team.files_system.model.enums.JoinStatus;
import com.conquer_team.files_system.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_folder")
public class UserFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @Enumerated(EnumType.STRING)
    private JoinStatus type ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = FileStatus.UNAVAILABLE;
        }
    }


}
