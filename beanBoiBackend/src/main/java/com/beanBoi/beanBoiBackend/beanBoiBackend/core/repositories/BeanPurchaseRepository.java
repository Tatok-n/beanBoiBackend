package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        beanPurchaseMap.put("pricePaid", Float.toString(beanPurchase.getPricePaid()));
        beanPurchaseMap.put("beans", beanRepository.getAsMap(beanPurchase.getBeansPurchased()));
        beanPurchaseMap.put("isActive", beanPurchase.isActive());
        beanPurchaseMap.put("uid", beanPurchase.getUid());
        beanPurchaseMap.put("id", beanPurchase.getId());
        beanPurchaseMap.put("purchaseDate", beanPurchase.getPurchaseDate().toString());
        beanPurchaseMap.put("roastDate", beanPurchase.getRoastDate().toString());
        beanPurchaseMap.put("name", beanPurchase.getName());
        return beanPurchaseMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        BeanPurchase beanPurchase = new BeanPurchase();
        beanPurchase.setAmountRemaining(Float.parseFloat(map.get("amountRemaining").toString()));
        beanPurchase.setAmountPurchased(Float.parseFloat(map.get("amountPurchased").toString()));
        beanPurchase.setPricePaid(Float.parseFloat(map.get("pricePaid").toString()));
        beanPurchase.setRoastDate(LocalDate.parse(map.get("roastDate").toString()));
        beanPurchase.setPurchaseDate(LocalDate.parse(map.get("purchaseDate").toString()));
        beanPurchase.setUid((String) map.get("uid"));
        beanPurchase.setName(map.get("name").toString());

        Bean beans = beanRepository.verifyBean((DocumentReference)map.get("beans"));
        beanPurchase.setBeansPurchased(beans);
        beanPurchase.setActive(Boolean.parseBoolean(map.get("isActive").toString()));
        beanPurchase.setId((String) map.get("id"));
        return beanPurchase;
    }

    @Override
    public DocumentReference saveDocument(DocumentData data) {
        if (data.getId() == null) {
            data.setId(getNewId());
        }

        DocumentReference purchasedBeans = beanRepository.saveDocument(((BeanPurchase) data).getBeansPurchased());
        Map<String, Object> beanPurchaseMap = getAsMap(data);
        beanPurchaseMap.put("beans", purchasedBeans);
        return firestoreImplementation.addDocumentToCollectionWithId(collectionName, beanPurchaseMap,data.getId());

    }

    public List<BeanPurchase> getActiveBeanPurchaseForUser(String uid) {
        CollectionReference collectionReference = firestoreImplementation.getCollectionReference(collectionName);
        Query isActive =  collectionReference.whereEqualTo("uid", uid).whereEqualTo("isActive", true);
        ApiFuture<QuerySnapshot> querySnapshot = isActive.get();
        try {
            return querySnapshot.get().getDocuments().stream()
                    .map(queryDocumentSnapshot ->  (BeanPurchase) getFromMap(queryDocumentSnapshot.getData()))
                    .toList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
