package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;


import com.beanBoi.beanBoiBackend.beanBoiBackend.TestUtils;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class testGrinderRepository extends TestUtils {
    @Autowired
    private GrinderRepository grinderRepository;

    @Test
    public void testSaveGrinder() {
        //Setup
        Grinder testGrinder = getTestGrinder();

        //Execute
        DocumentReference documentReference = grinderRepository.saveDocument(testGrinder);
        String id = documentReference.getId();

        //Assert
        getTestGrinderMap().keySet().forEach(key ->
        {
            Assertions.assertEquals(getTestGrinderMap().get(key).toString(), firestore.getDocument(grinderRepository.collectionName, id).get(key).toString());});
    }

    @Test
    public void testGetGrinder() {
        //Setup
        Grinder testGrinder = getTestGrinder();
        DocumentReference documentReference = grinderRepository.saveDocument(testGrinder);
        usedCollections.add(grinderRepository.collectionName);

        //Execute
        Grinder retrievedGrinder = grinderRepository.getGrinderById(documentReference.getId());
        testGrinder.setId(retrievedGrinder.getId());

        //Assert
        assertEquals(testGrinder, retrievedGrinder);

    }

    @Test
    public void testDeleteGrinder() {
        //Setup
        Grinder testGrinder = getTestGrinder();
        DocumentReference documentReference = grinderRepository.saveDocument(testGrinder);
        testGrinder.setId(documentReference.getId());
        grinderRepository.deleteDocument(testGrinder);

        //Execute
        Grinder retrievedGrinder = grinderRepository.verifyGrinder(documentReference);

        //Assert
        assertEquals("DELETED", retrievedGrinder.getName());

    }
}
