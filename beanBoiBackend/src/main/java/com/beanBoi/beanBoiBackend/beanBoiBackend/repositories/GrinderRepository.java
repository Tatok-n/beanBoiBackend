package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Grinder;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GrinderRepository extends DocumentRepository{

    private final FirestoreImplementation firestoreImplementation;

    public GrinderRepository(FirestoreImplementation firestoreImplementation) {
        this.collectionName = "grinders";
        this.firestoreImplementation = firestoreImplementation;
    }

    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        Grinder grinder = (Grinder) data;
        Map<String, Object> grinderMap = new HashMap<>();
        grinderMap.put("name", grinder.getName());
        grinderMap.put("isActive", grinder.isActive());
        grinderMap.put("settings", grinder.getGrindSetting());
        return grinderMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Grinder grinder = new Grinder();
        grinder.setName((String) map.get("name"));
        grinder.setActive((boolean) map.get("isActive"));
        grinder.setGrindSetting((List<String>) map.get("settings"));
        return grinder;
    }

    public Grinder getGrinderById(String grinderId) {
        return (Grinder) getDocumentById(grinderId);
    }

    public Grinder verifyGrinder(DocumentReference reference) {
        Grinder grinder = new Grinder();
        if (firestoreImplementation.getDocumentFromReference(reference).getData() == null) {
            grinder.setName("DELETED");
        } else {
            grinder = getGrinderById(firestoreImplementation.getDocumentFromReference(reference).getId());
        }
        return grinder;
    }
}
