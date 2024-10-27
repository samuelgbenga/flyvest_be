package com.flyvestmobile.flyvest.mobile.application.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorDetailsResponse {


    private String expertise;
    private double averageRating;
    private String fullName;
    private String email;
    private String profilePicture;
    private String country;
}
