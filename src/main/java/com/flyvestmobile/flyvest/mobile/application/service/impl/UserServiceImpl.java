package com.flyvestmobile.flyvest.mobile.application.service.impl;

import com.cloudinary.Api;
import com.flyvestmobile.flyvest.mobile.application.entity.Mentor;
import com.flyvestmobile.flyvest.mobile.application.entity.Rating;
import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.enums.Role;
import com.flyvestmobile.flyvest.mobile.application.exceptions.AlreadyExistsException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.InvalidInputException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotFoundException;
import com.flyvestmobile.flyvest.mobile.application.payload.request.AddMentorRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.EmailDetails;
import com.flyvestmobile.flyvest.mobile.application.payload.request.RatingRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.ApiResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.MentorDetailsResponse;
import com.flyvestmobile.flyvest.mobile.application.repository.MentorRepository;
import com.flyvestmobile.flyvest.mobile.application.repository.RatingRepository;
import com.flyvestmobile.flyvest.mobile.application.repository.UserRepository;
import com.flyvestmobile.flyvest.mobile.application.service.EmailService;
import com.flyvestmobile.flyvest.mobile.application.service.FileUploadService;
import com.flyvestmobile.flyvest.mobile.application.service.UserService;
import com.flyvestmobile.flyvest.mobile.application.utils.AccountUtils;
import com.flyvestmobile.flyvest.mobile.application.utils.EmailBody;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MentorRepository mentorRepository;
    private final EmailService emailService;
    private final FileUploadService fileUploadService;
    private final RatingRepository ratingRepository;

    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    public ApiResponse addMentor(AddMentorRequest request, String email) {


        // Find authenticated user
        User authenticatedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));

        // Check if the authenticated user is an admin
        boolean isAdmin = authenticatedUser.getRoles().stream()
                .anyMatch(role -> role == Role.ADMIN);

        if (!isAdmin) {
            throw new AccessDeniedException("You do not have permission to add a mentor");
        }

        // Check if the mentor already exists by email
        Optional<User> existingMentor = userRepository.findByEmail(request.getEmail());
        if (existingMentor.isPresent()) {
            throw new AlreadyExistsException("User already exists, please Login");
        }

        // Upload profile picture if provided
        String profilePictureUrl = null;
        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            try {
                profilePictureUrl = fileUploadService.uploadFile(request.getProfilePicture());
            } catch (FileSizeLimitExceededException e) {
                throw new InvalidInputException("Profile picture file size exceeds the allowable limit.");
            } catch (IOException e) {
                throw new InvalidInputException("An error occurred while uploading the profile picture. Please try again.");
            }
        }

        String generatedPassword = AccountUtils.generatePassword();


        // Create the Mentor entity with additional mentor-specific fields
        Mentor mentor = Mentor.builder()
                .fullName(request.getFirstname()+ " " + request.getLastname())
                .email(request.getEmail())
                .profilePicture(profilePictureUrl)
                .password(passwordEncoder.encode(generatedPassword)) // Encrypt password
                .country(request.getCountry())
                .role(Role.MENTOR)  // Assign MENTOR role
                .enabled(true)
                .expertise(request.getExpertise())
                .averageRating(0.0)  // Start with a zero rating
                   // Associate the saved User
                .build();

        // Save and return the Mentor entity
        Mentor savedMentor = mentorRepository.save(mentor);

        // Send a welcome email
        // Set up email message for the registered user/employee
        String userLoginUrl = baseUrl + "/user/login";

        String emailContent = EmailBody.addMentorEmail(savedMentor.getFullName(), mentor.getEmail(), generatedPassword, userLoginUrl);
        EmailDetails emailDetails = EmailDetails.builder()
                .fullName(mentor.getFullName())
                .recipient(mentor.getEmail())
                .subject("Welcome to Flyvest")
                .messageBody(emailContent)
                .build();

        emailService.mimeMailMessage(emailDetails);

        return ApiResponse.builder()
                .responseCode("200")
                .responseMessage("Mentor has been created successfully. Login details have been sent to their email.")
                .build();
    }

    @Override
    public String rateMentor(RatingRequest request, String email) {

        Rating rating = Rating.builder()
                .mentor(mentorRepository.findById(request.getMentorId()).orElseThrow(() -> new RuntimeException("Mentor not found")))
                .user(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")))
                .score(request.getScore())
                .comment(request.getComment())
                .build();

        ratingRepository.save(rating);

        // Update the mentor's average rating
        double averageRating = calculateAverageRating(request.getMentorId());
        Mentor mentor = mentorRepository.findById(request.getMentorId()).orElseThrow(() -> new NotFoundException("Mentor not found"));
        mentor.setAverageRating(averageRating);
        mentorRepository.save(mentor);

        return "Thank you for your rating";
    }

    // get mentor details by a user
    @Override
    public MentorDetailsResponse selectMentor(Long id) {

        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if(mentor == null){
            throw new NotFoundException("mentor not found");
        }


        return MentorDetailsResponse.builder()
                .fullName(mentor.getFullName())
                .country(mentor.getCountry())
                .averageRating(mentor.getAverageRating())
                .email(mentor.getEmail())
                .expertise(mentor.getExpertise())
                .profilePicture(mentor.getProfilePicture())
                .build();
    }





    private double calculateAverageRating(Long mentorId) {
        List<Rating> ratings = ratingRepository.findByMentorId(mentorId);
        if (ratings.isEmpty()) {
            return 0.0; // No ratings yet
        }
        double sum = ratings.stream().mapToInt(Rating::getScore).sum();
        return sum / ratings.size();
    }

}
