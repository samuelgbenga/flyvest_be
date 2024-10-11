package com.flyvestmobile.flyvest.mobile.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "notification_tbl")
public class Notification extends BaseEntity{

    private String message;
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recipient;

    // Constructors, Getters, Setters
}
