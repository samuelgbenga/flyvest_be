package com.flyvestmobile.flyvest.mobile.application.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@SuperBuilder
public class ConfirmationToken extends BaseEntity{


    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;


    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User users;


    public ConfirmationToken(User user){
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusDays(1);
        this.users = user;
    }
}
