package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepo extends JpaRepository<SocialLogin, Long> {
}
