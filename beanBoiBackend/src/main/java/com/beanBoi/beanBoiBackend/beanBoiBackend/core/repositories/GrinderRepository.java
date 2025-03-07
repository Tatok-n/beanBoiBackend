package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        grinderMap.put("uid", grinder.getUid());
        grinderMap.put("isActive", grinder.isActive());
        grinderMap.put("settings", grinder.getGrindSetting());
        grinderMap.put("id", grinder.getId());
        return grinderMap;
    }

    @Override
    public DocumentReference saveDocument(DocumentData data) {
        if (data.getId() == null) {
            data.setId(getNewId());
        }
        return firestoreImplementation.addDocumentToCollectionWithId(collectionName,getAsMap(data),data.getId());
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Grinder grinder = new Grinder();
        grinder.setName((String) map.get("name"));
        grinder.setUid((String) map.get("uid"));
        grinder.setActive((boolean) map.get("isActive"));
        grinder.setGrindSetting((List<String>) map.get("settings"));
        grinder.setId((String) map.get("id"));
        return grinder;
    }

    public List<Grinder> getGrindersForUser(String uid) {
        CollectionReference collectionReference = firestoreImplementation.getCollectionReference(collectionName);
        Query isActive =  collectionReference.whereEqualTo("uid", uid).whereEqualTo("isActive", true);
        ApiFuture<QuerySnapshot> querySnapshot = isActive.get();
        try {
            return querySnapshot.get().getDocuments().stream()
                    .map(queryDocumentSnapshot ->  (Grinder) getFromMap(queryDocumentSnapshot.getData()))
                    .toList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
