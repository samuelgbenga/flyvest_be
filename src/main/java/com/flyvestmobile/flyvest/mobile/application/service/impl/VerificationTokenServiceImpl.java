package com.flyvestmobile.flyvest.mobile.application.service.impl;

import com.flyvestmobile.flyvest.mobile.application.entity.ConfirmationToken;
import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.repository.ConfirmationTokenRepo;
import com.flyvestmobile.flyvest.mobile.application.repository.UserRepository;
import com.flyvestmobile.flyvest.mobile.application.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final UserRepository userRepository;

    @Override
    public String validateToken(String token) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepo.findByToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return "Invalid token";
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();

        if (confirmationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token has expired";
        }
        User user = confirmationToken.getUsers();
        user.setEnabled(true);
        userRepository.save(user);

        confirmationTokenRepo.delete(confirmationToken);

        return "Email confirmation is successful";
    }
}
