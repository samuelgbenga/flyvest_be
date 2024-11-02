package com.flyvestmobile.flyvest.mobile.application.service;

import com.flyvestmobile.flyvest.mobile.application.payload.request.AddMentorRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.BookingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.GoalRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.RatingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.ApiResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.BookResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.MentorDetailsResponse;

public interface UserService {

    ApiResponse addMentor(AddMentorRequest request, String email);

    String rateMentor(RatingRequest request, String email);

    MentorDetailsResponse selectMentor(Long id);

    ApiResponse bookMentor(String email, BookingRequest request);

    ApiResponse createGoal(String email, GoalRequest goalRequest);






}
