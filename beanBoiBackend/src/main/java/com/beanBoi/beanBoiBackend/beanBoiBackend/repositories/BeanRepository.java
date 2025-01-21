package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.models.DocumentData;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
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
        beanMap.put("Roaster", bean.getRoaster());
        beanMap.put("altitude", bean.getAltitude());
        beanMap.put("price", Float.toString(bean.getPrice()));
        beanMap.put("roastDegree", bean.getRoastDegree());
        beanMap.put("tastingNotes", bean.getTastingNotes());
        beanMap.put("isActive", bean.isActive());
        return beanMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Bean bean = new Bean();
        bean.setName(map.get("Name").toString());
        bean.setRoaster(map.get("Roaster").toString());
        bean.setTastingNotes(map.get("tastingNotes").toString());
        bean.setAltitude(Long.parseLong(map.get("altitude").toString()));
        bean.setPrice(Float.parseFloat(map.get("price").toString()));
        bean.setRoastDegree(Long.parseLong(map.get("roastDegree").toString()));
        bean.setActive(Boolean.parseBoolean((map.get("isActive").toString())));
        return bean;
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
