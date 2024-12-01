package com.conquer_team.files_system.model.entity;

import com.conquer_team.files_system.model.enums.EventTypes;
import com.conquer_team.files_system.model.enums.MessageStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "out_box")
public class OutBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private EventTypes type;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @CreationTimestamp
    private LocalDateTime created_At;
}
