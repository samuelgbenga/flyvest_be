package com.flyvestmobile.flyvest.mobile.application.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "rating_tbl")
public class Rating extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int score; // Rating score (e.g., 1 to 5)
    
    private String comment; // Optional feedback from the user
}
