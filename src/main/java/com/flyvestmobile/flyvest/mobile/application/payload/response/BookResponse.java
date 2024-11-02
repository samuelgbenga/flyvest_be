package com.flyvestmobile.flyvest.mobile.application.payload.response;


import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Long bookingId;

    private LocalDateTime sessionDateTime;


    private User user;


    private String status;
}
