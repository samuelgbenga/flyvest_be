package com.flyvestmobile.flyvest.mobile.application.service;

import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;

public interface AuthService {

    LoginResponse loginUser(LoginRequest loginRequest);
}
