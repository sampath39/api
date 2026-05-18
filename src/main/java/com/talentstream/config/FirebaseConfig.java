package com.talentstream.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {

            java.io.File file = new java.io.File("/home/ubuntu/firebase-services-account.json");
            if (!file.exists()) {
                System.err.println("Firebase credentials not found at " + file.getAbsolutePath() + ". Skipping Firebase initialization.");
                return;
            }

            try (FileInputStream serviceAccount = new FileInputStream(file)) {

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(
                                GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);

                System.out.println("Firebase initialized successfully!");

            }
        }
    }
}