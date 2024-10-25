package com.flyvestmobile.flyvest.mobile.application.entity;

import com.flyvestmobile.flyvest.mobile.application.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@Entity
@Table(name = "verification_token")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VerificationToken extends BaseEntity {

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

}
