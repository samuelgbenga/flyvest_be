package com.flyvestmobile.flyvest.mobile.application.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Mentor extends User {

    private String expertise;
    private double averageRating;

    @OneToMany(mappedBy = "mentor")
    private List<Booking> bookings;
}
