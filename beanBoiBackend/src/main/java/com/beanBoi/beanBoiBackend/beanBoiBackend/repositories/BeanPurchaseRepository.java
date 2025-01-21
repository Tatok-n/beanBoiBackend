package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.models.DocumentData;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BeanPurchaseRepository extends DocumentRepository {

    @Autowired
    private FirestoreImplementation firestoreImplementation;
    @Autowired
    private BeanRepository beanRepository;

    public BeanPurchaseRepository() {
        this.collectionName = "beanPurchases";
    }


    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        BeanPurchase beanPurchase = (BeanPurchase) data;
        Map<String, Object> beanPurchaseMap = new HashMap<>();
        beanPurchaseMap.put("amountPurchased", Float.toString(beanPurchase.getAmountPurchased()));
        beanPurchaseMap.put("amountRemaining", Float.toString(beanPurchase.getAmountRemaining()));
        beanPurchaseMap.put("beans", beanRepository.getAsMap(beanPurchase.getBeansPurchased()));
        beanPurchaseMap.put("isActive", beanPurchase.isActive());
        beanPurchaseMap.put("uid", beanPurchase.getUid());
        return beanPurchaseMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        BeanPurchase beanPurchase = new BeanPurchase();
        beanPurchase.setAmountRemaining(Float.parseFloat(map.get("amountRemaining").toString()));
        beanPurchase.setAmountPurchased(Float.parseFloat(map.get("amountPurchased").toString()));
        beanPurchase.setUid((String) map.get("uid"));

        Bean beans = beanRepository.verifyBean((DocumentReference)map.get("beans"));
        beanPurchase.setBeansPurchased(beans);
        beanPurchase.setActive(Boolean.parseBoolean(map.get("isActive").toString()));
        return beanPurchase;
    }


    public BeanPurchase verifyPurchase(DocumentReference reference) {
        BeanPurchase purchase = new BeanPurchase();
        if (firestoreImplementation.getDocumentFromReference(reference).getData() == null) {
            purchase.setAmountRemaining(-1);
        } else {
            purchase = getBeanPurchaseById(firestoreImplementation.getDocumentFromReference(reference).getId());
        }
        return purchase;
    }


    public BeanPurchase getBeanPurchaseById(String beanPurchaseId) {
        return (BeanPurchase) getDocumentById(beanPurchaseId);
    }





}
