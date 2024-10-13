package com.flyvestmobile.flyvest.mobile.application.controller;


import com.flyvestmobile.flyvest.mobile.application.exceptions.NotFoundException;
import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;
import com.flyvestmobile.flyvest.mobile.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser( @RequestBody LoginRequest loginRequest)  {

        try {
            LoginResponse loginResponse = authService.loginUser(loginRequest);
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e){
            throw new NotFoundException("User not found");
        }

    }

    @GetMapping("/nothing")
    public ResponseEntity<?> getNothing()  {
        return ResponseEntity.ok("authService.loginUser(loginRequest)");

    }
}
