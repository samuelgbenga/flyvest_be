package com.flyvestmobile.flyvest.mobile.application.service.impl;

import com.flyvestmobile.flyvest.mobile.application.entity.Mentor;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotFoundException;
import com.flyvestmobile.flyvest.mobile.application.payload.response.BookResponse;
import com.flyvestmobile.flyvest.mobile.application.repository.MentorRepository;
import com.flyvestmobile.flyvest.mobile.application.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    @Override
    public List<BookResponse> getBookingCal(String email) {

        Mentor mentor = mentorRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Mentor not found Yes!"));

        return mentor.getBookings().stream().map(booking ->
             new BookResponse(
                   booking.getId(),
                   booking.getSessionDateTime(),
                   booking.getUser(),
                   booking.getStatus().name()
            )
        ).collect(Collectors.toList());

    }
}
