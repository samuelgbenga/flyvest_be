package com.flyvestmobile.flyvest.mobile.application.controller;

import com.flyvestmobile.flyvest.mobile.application.payload.request.AddMentorRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.RatingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.ApiResponse;
import com.flyvestmobile.flyvest.mobile.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

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

}
