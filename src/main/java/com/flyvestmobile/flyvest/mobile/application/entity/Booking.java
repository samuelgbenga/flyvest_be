package com.flyvestmobile.flyvest.mobile.application.entity;

import com.flyvestmobile.flyvest.mobile.application.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "booking_tbl")
public class Booking extends BaseEntity{

    private LocalDate sessionDate;
    private String sessionTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    private Status status;
}
