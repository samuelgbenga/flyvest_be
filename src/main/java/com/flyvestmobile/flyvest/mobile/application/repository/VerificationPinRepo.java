package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.entity.VerificationPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationPinRepo extends JpaRepository<VerificationPin, Long> {
    Optional<VerificationPin> findByUserAndPin(User user, int pin);
}
