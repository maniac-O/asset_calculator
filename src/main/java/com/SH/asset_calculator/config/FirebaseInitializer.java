package com.SH.asset_calculator.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseInitializer {
    /*
    @Bean
    public void getFirebaseAuth() throws IOException, FirebaseAuthException {

        FirebaseApp.initializeApp();

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("cj456456@gmail.com")
                .setEmailVerified(false)
                .setPassword("whrhkdgus2")
                .setPhoneNumber("+821051153154");

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());


    } */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("./serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("asset-calculator.appspot.com")
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(options);
        return app;
    }


    @Bean
    public FirebaseAuth getFirebaseAuth() throws IOException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
        return firebaseAuth;
    }
}
