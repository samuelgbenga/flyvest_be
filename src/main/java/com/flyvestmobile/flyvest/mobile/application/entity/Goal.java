package com.flyvestmobile.flyvest.mobile.application.entity;


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
@Table(name = "goal_tbl")
public class Goal extends BaseEntity{

    private String goalName;
    private String goalDescription;
    private Double targetAmount;
    private LocalDate targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
