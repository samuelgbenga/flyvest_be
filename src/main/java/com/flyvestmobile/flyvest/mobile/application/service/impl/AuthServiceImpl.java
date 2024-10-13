package com.flyvestmobile.flyvest.mobile.application.service.impl;

import com.flyvestmobile.flyvest.mobile.application.config.JwtService;
import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.entity.VerificationToken;
import com.flyvestmobile.flyvest.mobile.application.enums.TokenType;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotEnabledException;
import com.flyvestmobile.flyvest.mobile.application.exceptions.NotFoundException;
import com.flyvestmobile.flyvest.mobile.application.payload.request.LoginRequest;
import com.flyvestmobile.flyvest.mobile.application.payload.response.LoginResponse;
import com.flyvestmobile.flyvest.mobile.application.repository.UserRepository;
import com.flyvestmobile.flyvest.mobile.application.repository.VerificationTokenRepo;
import com.flyvestmobile.flyvest.mobile.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final VerificationTokenRepo verificationTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User person = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("User is not found"));

        if (!person.isEnabled()){
            throw new NotEnabledException("User account is not enabled. Please check your email to confirm your account.");
        }

        var jwtToken = jwtService.generateToken(person);
        revokeAllUserTokens(person);
        saveUserToken(person, jwtToken);




        return LoginResponse.builder()
                .responseCode("002")
                .responseMessage("Your have been logged in successfully")
                .token(jwtToken)
                .build();
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
