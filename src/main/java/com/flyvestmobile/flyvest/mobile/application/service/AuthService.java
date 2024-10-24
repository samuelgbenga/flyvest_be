package com.flyvestmobile.flyvest.mobile.application.service;

import com.flyvestmobile.flyvest.mobile.application.payload.request.AuthRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.ForgotPasswordRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.AuthResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    AuthResponse register(AuthRequest authRequest) throws MessagingException;

    LoginResponse login (LoginRequest loginRequest);

    String forgotPassword(ForgotPasswordRequest email);

    public String resetPassword(String email, int pin, String newPassword);
}
