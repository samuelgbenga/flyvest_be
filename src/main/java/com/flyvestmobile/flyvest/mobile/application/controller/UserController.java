package com.flyvestmobile.flyvest.mobile.application.controller;

import com.flyvestmobile.flyvest.mobile.application.payload.request.AddMentorRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.BookingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.GoalRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.RatingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.ApiResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.BookResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.MentorDetailsResponse;
import com.flyvestmobile.flyvest.mobile.application.service.MentorService;
import com.flyvestmobile.flyvest.mobile.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final MentorService mentorService;

    @PostMapping(value = "/add-mentor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMentor(
            @ModelAttribute AddMentorRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ApiResponse response = userService.addMentor(request, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{mentorId}/rate")
    public ResponseEntity<String> rateMentor(@RequestBody RatingRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String response = userService.rateMentor(request, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mentor")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> selectMentor(@RequestParam Long mentorId){

        MentorDetailsResponse mentor = userService.selectMentor(mentorId);

        return ResponseEntity.ok(mentor);
    }

    @PostMapping("/mentor/booking")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> bookMentor(@RequestBody BookingRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ApiResponse result = userService.bookMentor(email, request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/goal-setting")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> goalSetting(@RequestBody GoalRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ApiResponse result = userService.createGoal(email, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mentor/get-bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getBookings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<BookResponse> result = mentorService.getBookingCal(email);
        return ResponseEntity.ok(result);
    }


}
