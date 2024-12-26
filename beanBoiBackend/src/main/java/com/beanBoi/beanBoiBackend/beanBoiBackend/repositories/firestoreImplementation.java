package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import java.io.FileInputStream;
import java.io.IOException;

public class firestoreImplementation {
    private String serviceAcccountPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
    private String projectId = System.getenv("BEANBOI_PROJECTID");


    public Firestore init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(serviceAcccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
}
