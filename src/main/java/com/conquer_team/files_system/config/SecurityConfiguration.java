package com.conquer_team.files_system.config;

import com.conquer_team.files_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(
                                        "/api/v1/auth/**",
                                        "/api/v1/downloads/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/configuration/security",
                                        "/configuration/ui",
                                        "/swagger-ui/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                )
                                .permitAll()
//                                .requestMatchers("/api/v1/plan/**").hasAuthority("MANAGER")
//                                .requestMatchers(
//                                        "/api/v1/admin/**",
//                                        "/api/v1/finger_places/**",
//                                        "/api/v1/sections/**",
//                                        "/api/v1/timezones/**",
//                                        "/api/v1/employees/**",
//                                        "/api/v1/employee_moves/**"
//                                ).hasAnyAuthority("ADMIN")
//                                .requestMatchers("/api/v1/user/**").hasAnyAuthority("CAN_PRINT")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
//                new ClassPathResource("google_services.json")
//                        .getInputStream()
//        );
//
//        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
//                .setCredentials(googleCredentials)
//                .build();
//
//        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
//
//        return FirebaseMessaging.getInstance(app);
//    }
}
