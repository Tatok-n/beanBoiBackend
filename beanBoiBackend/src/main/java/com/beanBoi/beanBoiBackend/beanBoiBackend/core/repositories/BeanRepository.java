package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;


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
        beanMap.put("Name", bean.getName());
        beanMap.put("uid", bean.getUid());
        beanMap.put("Roaster", bean.getRoaster());
        beanMap.put("altitude", bean.getAltitude());
        beanMap.put("price", (bean.getPrice()));
        beanMap.put("roastDegree", bean.getRoastDegree());
        beanMap.put("tastingNotes", bean.getTastingNotes());
        beanMap.put("isActive", bean.isActive());
        beanMap.put("id", bean.getId());

        return beanMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Bean bean = new Bean();
        bean.setName(map.get("Name").toString());
        bean.setUid(map.get("uid").toString());
        bean.setRoaster(map.get("Roaster").toString());
        bean.setTastingNotes(map.get("tastingNotes").toString());
        bean.setAltitude(Long.parseLong(map.get("altitude").toString()));
        bean.setPrice(Float.parseFloat(map.get("price").toString()));
        bean.setRoastDegree(Long.parseLong(map.get("roastDegree").toString()));
        bean.setActive(Boolean.parseBoolean((map.get("isActive").toString())));
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
