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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

         http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers(antMatcher(HttpMethod.GET, "/swagger-ui.html"),
                                    antMatcher(HttpMethod.GET, "/"),
                            antMatcher(HttpMethod.GET, "/swagger-ui/**"),
                            antMatcher(HttpMethod.GET, "/v3/api-docs/**"),
                            antMatcher(HttpMethod.GET, "/swagger-resources/**"))

                            .permitAll();
                    auth.requestMatchers("/favicon.ico").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(withDefaults())
                 .exceptionHandling(exception -> exception
                         .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                 .sessionManagement(session -> session
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .httpBasic(Customizer.withDefaults());

        http.authenticationProvider(authenticationProvider);



        return http.build();
    }
}