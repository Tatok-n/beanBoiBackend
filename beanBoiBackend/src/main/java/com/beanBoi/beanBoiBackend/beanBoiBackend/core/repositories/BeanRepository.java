package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
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
public class BeanRepository extends DocumentRepository{


    private final FirestoreImplementation firestoreImplementation;

    public BeanRepository(FirestoreImplementation firestoreImplementation) {
        this.collectionName = "beans";
        this.firestoreImplementation = firestoreImplementation;
    }

    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        Bean bean = (Bean) data;
        Map<String, Object> beanMap = new HashMap<>();
        beanMap.put("name", bean.getName());
        beanMap.put("process", bean.getProcess());
        beanMap.put("origin", bean.getOrigin());
        beanMap.put("uid", bean.getUid());
        beanMap.put("roaster", bean.getRoaster());
        beanMap.put("altitude", bean.getAltitude());
        beanMap.put("price", (bean.getPrice()));
        beanMap.put("roastDegree", bean.getRoastDegree());
        beanMap.put("tastingNotes", bean.getTastingNotes());
        beanMap.put("isActive", bean.isActive());
        beanMap.put("timesPurchased", bean.getTimesPurchased());
        beanMap.put("id", bean.getId());

        return beanMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Bean bean = new Bean();
        bean.setName(map.get("name").toString());
        bean.setProcess(map.get("process").toString());
        bean.setOrigin(map.get("origin").toString());
        bean.setUid(map.get("uid").toString());
        bean.setRoaster(map.get("roaster").toString());
        bean.setTastingNotes(map.get("tastingNotes").toString());
        bean.setAltitude(Long.parseLong(map.get("altitude").toString()));
        bean.setPrice(Float.parseFloat(map.get("price").toString()));
        bean.setRoastDegree(Long.parseLong(map.get("roastDegree").toString()));
        bean.setActive(Boolean.parseBoolean((map.get("isActive").toString())));
        bean.setTimesPurchased(Integer.parseInt((map.get("timesPurchased").toString())));
        bean.setId(map.get("id").toString());
        return bean;
    }

    @Override
    public DocumentReference saveDocument(DocumentData data) {
        if (data.getId() == null) {
            data.setId(getNewId());
        }
        return firestoreImplementation.addDocumentToCollectionWithId(collectionName,getAsMap(data), data.getId());
    }

    public Bean getBeanById(String beanId) {
        return (Bean) getDocumentById(beanId);
    }



    public List<Bean> getActiveBeansForUser(String uid) {
        CollectionReference collectionReference = firestoreImplementation.getCollectionReference(collectionName);
        Query isActive =  collectionReference.whereEqualTo("uid", uid).whereEqualTo("isActive", true);
        ApiFuture<QuerySnapshot> querySnapshot = isActive.get();
        try {

            return querySnapshot.get().getDocuments().stream()
                    .map(queryDocumentSnapshot ->  (Bean) getFromMap(queryDocumentSnapshot.getData()))
                    .toList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public Bean verifyBean(DocumentReference reference) {
        Bean bean = new Bean();
        if (firestoreImplementation.getDocumentFromReference(reference).getData() == null) {
            bean.setName("DELETED");
        } else {
            bean = getBeanById(firestoreImplementation.getDocumentFromReference(reference).getId());
        }
        return bean;
    }

}
