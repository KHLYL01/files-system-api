package com.conquer_team.files_system.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
public class FireBaseConfiguration {

//    @Value("${app.firebase-configuration-file}")
//    private String firebaseConfigPath;
//
//    @PostConstruct
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
//                new ClassPathResource(firebaseConfigPath)
//                        .getInputStream()
//        );
//
//        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
//                .setCredentials(googleCredentials)
//                .build();
//
//        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "file-system");
//
//        return FirebaseMessaging.getInstance(app);
//    }
}
