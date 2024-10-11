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
@Table(name = "task_tbl")
public class Task extends BaseEntity{

    private String taskName;
    private String description;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User mentee;

    // Constructors, Getters, Setters
}
