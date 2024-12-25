package com.beanBoi.beanBoiBackend.beanBoiBackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class manualTests {

    @Test public void manualTests() throws IOException, ExecutionException, InterruptedException {
        String serviceAcccountPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        String projectId = System.getenv("BEANBOI_PROJECTID");

        FileInputStream serviceAccount = new FileInputStream(serviceAcccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference userRef = db.collection("users").document("user0");
        ApiFuture<DocumentSnapshot> future = userRef.get();
        DocumentSnapshot document = future.get();
        List<DocumentReference> recipies = (List<DocumentReference>) document.getData().get("recipies");
        future = recipies.getFirst().get();
        document = future.get();

        if (document.exists()) {
            System.out.println(document.getData());
        }


    }
}
