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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfiguration {

    private static final String FIREBASE_CONFIG_PATH = "filemanager-446f0-firebase-adminsdk-omy7t-34b300117f.json";


//    @Bean
//    		try {
////			FirebaseOptions file = FirebaseOptions.builder().setCredentials(
////					GoogleCredentials.fromStream(this.getClass().getClassLoader().getResourceAsStream("fir-fcm-v1-firebase.json")))
////					.build();
////
////			System.out.println("Check File : "+file);
//
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/fir-fcm-v1-firebase.json");
//        //System.out.println("Service Acc File: "+serviceAccount);
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
//        //System.out.println("Got IT: "+options);
//        if(FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//            System.out.println("Firebase App initialized successfully!");
//        }else {
//            System.out.println("Else :Firebase App already initialized.");
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }

    @Bean
    void firebaseMessaging() throws IOException {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource(FIREBASE_CONFIG_PATH)
                            .getInputStream()
            );
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
                System.out.println("Firebase App initialized successfully!");
            }else{
                System.out.println("Else :Firebase App already initialized.");
            }
//            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
//            return FirebaseMessaging.getInstance(app);
        }catch(Exception e){
            e.getStackTrace();
        }
    }


//    @Bean
//    public GoogleCredentials googleCredentials() throws IOException {
//        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FIREBASE_CONFIG_PATH)) {
//            if (inputStream == null) {
//                throw new IllegalArgumentException("Firebase config file not found: " + FIREBASE_CONFIG_PATH);
//            }
//            return GoogleCredentials.fromStream(inputStream).createScoped("https://www.googleapis.com/auth/firebase.messaging");
//        }
//    }

//    @Value("${app.firebase-configuration-file}")
//    private String firebaseConfigPath;
//
}
