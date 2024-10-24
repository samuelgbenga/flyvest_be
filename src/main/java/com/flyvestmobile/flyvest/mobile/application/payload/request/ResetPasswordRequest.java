package com.flyvestmobile.flyvest.mobile.application.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private int pin;
    private String newPassword;
}