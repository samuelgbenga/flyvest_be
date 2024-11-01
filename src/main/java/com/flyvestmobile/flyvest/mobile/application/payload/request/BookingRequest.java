package com.flyvestmobile.flyvest.mobile.application.payload.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    private Long mentorId;
    private LocalDateTime localDateTime;

}
