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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

         http.csrf(CsrfConfigurer:: disable)
                 .authorizeHttpRequests(auth -> {
                     auth.requestMatchers(
                             "/api/auth/**",  // Allows all POST/GET requests to this path
                             "/swagger-ui.html",
                             "/swagger-ui/**",
                             "/v3/api-docs/**",
                             "/swagger-resources/**",
                             "/favicon.ico",
                             "/"
                     ).permitAll();
                    auth.anyRequest().authenticated();
                })
//                .oauth2Login(withDefaults())
                 .exceptionHandling(exception -> exception
                         .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                         .accessDeniedHandler(customAccessDeniedHandler)
                 )
                 .sessionManagement(session -> session
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .httpBasic(Customizer.withDefaults());

        // Enable OAuth2 login
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Specify a custom login page if you have one
                .permitAll()
        );

        // Enable form-based login (traditional login with username/password)
        http.formLogin(form -> form
                .loginPage("/login") // Specify a custom login page if needed
                .permitAll()
        );

        http.authenticationProvider(authenticationProvider);



        return http.build();
    }

}