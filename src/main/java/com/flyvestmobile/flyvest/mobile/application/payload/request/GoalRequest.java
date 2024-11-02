package com.flyvestmobile.flyvest.mobile.application.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalRequest {


    private String goalName;
    private String goalDescription;
    private Double targetAmount;
    private LocalDate targetDate;
}
