package com.flyvestmobile.flyvest.mobile.application.entity;

import com.flyvestmobile.flyvest.mobile.application.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "booking_tbl")
public class Booking extends BaseEntity{

    private LocalDateTime sessionDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    private Status status;
}
