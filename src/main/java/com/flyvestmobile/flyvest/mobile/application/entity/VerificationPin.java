package com.flyvestmobile.flyvest.mobile.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_pin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationPin extends BaseEntity {

    @Column(nullable = false)
    private int pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean expired;
    private boolean revoked;

    private LocalDateTime expiryDate; // PIN expiration time
}
