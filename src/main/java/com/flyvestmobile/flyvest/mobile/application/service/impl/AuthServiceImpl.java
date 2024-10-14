package com.flyvestmobile.flyvest.mobile.application.service.impl;

import com.flyvestmobile.flyvest.mobile.application.config.JwtService;
import com.flyvestmobile.flyvest.mobile.application.entity.ConfirmationToken;
import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.entity.VerificationToken;
import com.flyvestmobile.flyvest.mobile.application.enums.Role;
import com.flyvestmobile.flyvest.mobile.application.enums.TokenType;
import com.flyvestmobile.flyvest.mobile.application.exceptions.AlreadyExistsException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.InvalidInputException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotEnabledException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotFoundException;
import com.flyvestmobile.flyvest.mobile.application.payload.request.AuthRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.request.EmailDetails;
import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.AuthResponse;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;
import com.flyvestmobile.flyvest.mobile.application.repository.ConfirmationTokenRepo;
import com.flyvestmobile.flyvest.mobile.application.repository.UserRepository;
import com.flyvestmobile.flyvest.mobile.application.repository.VerificationTokenRepo;
import com.flyvestmobile.flyvest.mobile.application.service.AuthService;
import com.flyvestmobile.flyvest.mobile.application.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepo verificationTokenRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    @Override
    public AuthResponse register(AuthRequest authRequest) throws MessagingException {

        Optional<User> existingUser = userRepository.findByEmail(authRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("User already exists, please Login");
        }

        User user = User.builder()
                .fullName(authRequest.getFirstname() + " " + authRequest.getLastname())
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);


        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
        confirmationTokenRepo.save(confirmationToken);
        System.out.println(confirmationToken.getToken());

        String confirmationUrl = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken.getToken();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFUL")
                .messageBody("Congratulations! You account has been successfully created \n "
                        + user.getEmail() + "\n" + savedUser.getFullName())
                .build();

        emailService.sendSimpleMailMessage(emailDetails, savedUser.getFullName(), confirmationUrl);

        return AuthResponse.builder()
                .responseCode("001")
                .responseMessage("Confirm your email")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found with username: " + loginRequest.getEmail()));


            if (!user.isEnabled()) {
                throw new NotEnabledException("User account is not enabled. Please check your email to confirm your account.");
            }

            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return LoginResponse.builder()
                    .responseCode("002")
                    .responseMessage("Login Successfully")
                    .token(jwtToken)
                    .build();

        }catch (AuthenticationException e) {
            throw new InvalidInputException("Invalid username or password!!");
        }
    }

    private void saveUserToken(User user, String jwtToken){
        var token = VerificationToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        verificationTokenRepo.save(token);
    }
    private void revokeAllUserTokens(User user){
        var validUserTokens = verificationTokenRepo.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        verificationTokenRepo.saveAll(validUserTokens);
    }


}
