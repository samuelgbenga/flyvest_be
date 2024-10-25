package com.flyvestmobile.flyvest.mobile.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "social_login_tbl")
public class SocialLogin extends BaseEntity {


    private String provider; // e.g., GOOGLE, GITHUB
    private String providerUserId; // Unique user ID from provider

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Link to User (including mentors)
}
