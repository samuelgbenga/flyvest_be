package com.flyvestmobile.flyvest.mobile.application.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mentor_tbl")
public class Mentor extends BaseEntity{

    private String mentorName;
    private String expertise;
    private Double rating;

    @OneToMany(mappedBy = "mentor")
    private List<Booking> bookings;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
