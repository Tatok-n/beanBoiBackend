package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Configuration
public class FirestoreImplementation {

    private String serviceAcccountPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
    private String projectId = System.getenv("BEANBOI_PROJECTID");
    private Firestore firestore = null;

    private void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAcccountPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();
            FirebaseApp.initializeApp(options);
            this.firestore = FirestoreClient.getFirestore();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Firestore getFirestore() {
        if (firestore == null) {
            init();
        }
        return firestore;
    }

    public DocumentReference getDocumentReference(String collectionName, String documentId) {
        return getFirestore().collection(collectionName).document(documentId);
    }

    public CollectionReference getCollectionReference(String collectionName) {
        return getFirestore().collection(collectionName);
    }

    public DocumentSnapshot getDocument(String collectionName, String documentId) {
        DocumentReference documentReference = getCollectionReference(collectionName).document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }


}
