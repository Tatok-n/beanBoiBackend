package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.Bean;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class testBeanRepository extends TestUtils{
    @Autowired
    private BeanRepository beanRepository;
    @Autowired
    FirestoreImplementation firestore;


    @Test
    public void testSaveBean() {
        //Setup
        Bean testBean = getTestBean();

        //Execute
        DocumentReference documentReference = beanRepository.saveDocument(testBean);
        addedDocuments.add(documentReference);
        String id = documentReference.getId();

        //Assert
        getBeanMap().keySet().forEach(key ->
        {assertEquals(getBeanMap().get(key).toString(), firestore.getDocument(beanRepository.collectionName, id).get(key).toString());});
    }

    @Test
    public void testGetBean() {
        //Setup
        Bean testBean = getTestBean();
        DocumentReference documentReference = beanRepository.saveDocument(testBean);
        addedDocuments.add(documentReference);

        //Execute
        Bean retrievedBean = beanRepository.getBeanById(documentReference.getId());
        testBean.setId(retrievedBean.getId());

        //Assert
        assertEquals(testBean, retrievedBean);

    }

    @Test
    public void testGetDeletedBean() {
        //Setup
        Bean testBean = getTestBean();
        DocumentReference documentReference = beanRepository.saveDocument(testBean);
        testBean.setId(documentReference.getId());
        beanRepository.deleteDocument(testBean);

        //Execute
        Bean retrievedBean = beanRepository.verifyBean(documentReference);

        //Assert
        assertEquals("DELETED", retrievedBean.getName());

    }


}
