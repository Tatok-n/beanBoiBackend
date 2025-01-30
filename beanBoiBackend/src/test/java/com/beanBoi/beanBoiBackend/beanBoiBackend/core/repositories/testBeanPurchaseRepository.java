package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testBeanPurchaseRepository extends TestUtils {
    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    BeanRepository beanRepository;
    @Autowired
    FirestoreImplementation firestore;

    @Test
    public void testSaveBeanPurchase() {
        //Setup
        BeanPurchase testBeanPurchase = getTestBeanPurchase();

        //Execute
        DocumentReference documentReference = beanPurchaseRepository.saveDocument(testBeanPurchase);
        usedCollections.add(beanRepository.collectionName);
        usedCollections.add(beanPurchaseRepository.collectionName);

        String id = documentReference.getId();

        //Assert
        getBeanPurchaseMap().keySet().forEach(key ->
                {
                    if (key.equals("beans")) {
                        assertEquals(getBeanMap(),  beanPurchaseRepository.getAsMap(beanPurchaseRepository.getBeanPurchaseById(id)).get(key));
                    } else {
                        assertEquals(getBeanPurchaseMap().get(key).toString(), firestore.getDocument(beanPurchaseRepository.collectionName, id).get(key).toString());}
                }

        );
    }


    @Test
    public void testGetBeanPurchase() {
        //Setup
        BeanPurchase testBeanPurchase = getTestBeanPurchase();
        DocumentReference documentReference = beanPurchaseRepository.saveDocument(testBeanPurchase);

        //Execute
        BeanPurchase retrievedBeanPurchase = beanPurchaseRepository.getBeanPurchaseById(documentReference.getId());
        testBeanPurchase.setId(retrievedBeanPurchase.getId());

        //Assert
        assertEquals(testBeanPurchase, retrievedBeanPurchase);

    }

    @Test
    public void testGetDeletedBeanPurchase() {
        //Setup
        BeanPurchase testBeanPurchase = getTestBeanPurchase();
        DocumentReference documentReference = beanPurchaseRepository.saveDocument(testBeanPurchase);
        testBeanPurchase.setId(documentReference.getId());
        beanPurchaseRepository.deleteDocument(testBeanPurchase);

        //Execute
        BeanPurchase retrievedBeanPurchase = beanPurchaseRepository.verifyPurchase(documentReference);

        //Assert
        assertEquals(-1, retrievedBeanPurchase.getAmountRemaining());

    }
}
