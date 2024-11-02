package com.flyvestmobile.flyvest.mobile.application.service;

import com.flyvestmobile.flyvest.mobile.application.payload.response.BookResponse;

import java.util.List;

public interface MentorService {

    List<BookResponse> getBookingCal(String email);
}
