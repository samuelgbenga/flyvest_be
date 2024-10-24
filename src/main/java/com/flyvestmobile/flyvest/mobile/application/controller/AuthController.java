package com.flyvestmobile.flyvest.mobile.application.controller;

import com.flyvestmobile.flyvest.mobile.application.payload.request.AuthRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.ForgotPasswordRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.ResetPasswordRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.AuthResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;
import com.flyvestmobile.flyvest.mobile.application.service.AuthService;
import com.flyvestmobile.flyvest.mobile.application.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")

public class AuthController {

    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest request) {

        try {
            AuthResponse registerUser = authService.register(request);
            if (!registerUser.equals("Invalid Email domain")) {
                return ResponseEntity.ok(registerUser);
            } else {
                return ResponseEntity.badRequest().body(registerUser);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login-user")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {

        String result = verificationTokenService.validateToken(token);
        if ("Email confirmed successfully".equals(result)) {
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", result));
        }

    }


    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgotPasswordRequest request){

        String response = authService.forgotPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String response = authService.resetPassword(
                request.getEmail(),
                request.getPin(),
                request.getNewPassword()
        );
        return ResponseEntity.ok(response);
    }

}