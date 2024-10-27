package com.flyvestmobile.flyvest.mobile.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()
                            .requestMatchers(
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/favicon.ico"
                            ).permitAll()
                            // Allow access to the login page
                            .requestMatchers("/login").permitAll()
                            .anyRequest().authenticated();
                })

                // OAuth2 Login Configuration for Google and GitHub
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // Custom login page endpoint
                        .successHandler((request, response, authentication) -> {
                            // On successful OAuth2 login, generate and add JWT to the response
                            String token;
                            if (authentication instanceof OAuth2AuthenticationToken) {
                                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                                token = jwtService.generateToken(oAuth2User);
                            }

                            else{
                                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                token = jwtService.generateToken(userDetails);
                            }


                            response.addHeader("Authorization", "Bearer " + token);
                        })
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        http.authenticationProvider(authenticationProvider);

        return http.build();
    }
}