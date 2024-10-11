package com.flyvestmobile.flyvest.mobile.application.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "thread_tbl")
public class Thread extends BaseEntity{


    private String title;
    private String content;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
