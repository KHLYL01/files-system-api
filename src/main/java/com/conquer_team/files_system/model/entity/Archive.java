package com.conquer_team.files_system.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "archives")
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_size")
    private double size;

    @Column(name = "update_type")
    private String type;

    @Column(name = "num_of_update_lines")
    private int numOfUpdateLines;

    private String details;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public String getUser() {
        return user.getFullname();
    }

    // طريقة لتحويل الكائن file إلى نص
    public String getFile() {
        return file.getName();
    }

}