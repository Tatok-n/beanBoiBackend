package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class TestUtils {
    static List<DocumentReference> addedDocuments =  new ArrayList<>();
    String testCollection = "test data";

    public Bean getTestBean() {
        Bean bean = new Bean();
        bean.setName("TanzaniaPeaberry");
        bean.setAltitude(1200);
        bean.setPrice(12.5f);
        bean.setRoaster("Faro");
        bean.setActive(true);
        bean.setRoastDegree(5);
        bean.setTastingNotes("Good stuff");
        return bean;
    }

    public HashMap<String, Object> getBeanMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", "TanzaniaPeaberry");
        map.put("altitude", 1200);
        map.put("price", 12.5f);
        map.put("Roaster", "Faro");
        map.put("isActive", true);
        map.put("roastDegree", 5);
        map.put("tastingNotes", "Good stuff");
        return map;
    }


    @AfterAll
    public static void clearDb() {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreImplementation firestoreImplementation = new FirestoreImplementation();
        firestoreImplementation.setFirestore(firestore);
        addedDocuments.forEach(documentReference -> firestoreImplementation.deleteDocument(documentReference.getPath().split("/")[0], documentReference.getId()));
    }


}
