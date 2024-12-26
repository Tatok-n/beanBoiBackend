package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BeanRepository {
    @Autowired
    private final FirestoreImplementation firestoreImplementation;
    String beanCollection = "beans";

    public BeanRepository(FirestoreImplementation firestoreImplementation) {
        this.firestoreImplementation = firestoreImplementation;
    }


    public Bean getBeanById(String beanId) {
        DocumentSnapshot document = firestoreImplementation.getDocument(beanCollection,beanId);
        Bean bean = new Bean();
        bean.setId(beanId);
        bean.setName(document.getString("Name"));
        bean.setRoaster(document.getString("Roaster"));
        bean.setAltitude(document.getLong("altitude"));
        bean.setPrice(Float.parseFloat(document.getString("price")));
        bean.setRoastDegree(document.getLong("roastDegree"));
        bean.setTastingNotes(document.getString("tastingNotes"));

        return bean;

    }

}
