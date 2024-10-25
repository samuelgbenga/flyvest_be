package com.flyvestmobile.flyvest.mobile.application.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRequest {
    private Long mentorId;
    private int score;
    private String comment;
}
