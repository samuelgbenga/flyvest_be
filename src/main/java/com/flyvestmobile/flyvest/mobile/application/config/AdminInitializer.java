package com.flyvestmobile.flyvest.mobile.application.config;


import com.flyvestmobile.flyvest.mobile.application.entity.User;
import com.flyvestmobile.flyvest.mobile.application.enums.Role;
import com.flyvestmobile.flyvest.mobile.application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${flyvest.admin.email}")
    private String adminEmail;

    @Value("${flyvest.admin.password}")
    private String adminPassword;

    @Value("${flyvest.admin.fullname}")
    private String adminFullname;

    // Constructor injection
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    CommandLineRunner initDatabase() {
        return args -> {

            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User adminUser = new User();
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRoles(roles);
                adminUser.setEnabled(true);
                adminUser.setFullName(adminFullname);

                userRepository.save(adminUser);

                logger.info("Admin user seeded into the database.");
            } else {
                logger.info("Admin user already exists.");
            }

        };
    }
}