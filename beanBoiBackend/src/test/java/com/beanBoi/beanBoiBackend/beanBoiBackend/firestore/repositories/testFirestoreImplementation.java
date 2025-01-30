package com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.TestUtils;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class testFirestoreImplementation extends TestUtils {
    @Autowired
    private BeanRepository beanRepository;


    @Test
    public void testAddToANonExistentCollection() {
        //Execute
        Exception exception = assertThrows(FileNotFoundException.class,() -> firestore.addDocumentToCollection("FAKE COLLECTION", new HashMap<>()));

        //Assert
        assertEquals("Collection FAKE COLLECTION does not exist!", exception.getMessage());
    }


    @Test
    public void testAddToCollection() {
        //Execute
        DocumentReference documentReference = firestore.addDocumentToNewCollection(testCollection, getBeanMap());
        HashMap<String, Object> beanmap = getBeanMap();
        beanmap.put("id", documentReference.getId());


        //Assert
        assertEquals(documentReference,firestore.getFirestore().collection(testCollection).document(beanmap.get("id").toString()));
    }

    @Test
    public void testGetDocument() {
        //Setup
        DocumentReference documentReference = firestore.addDocumentToNewCollection(testCollection, getBeanMap());


        //Execute
        DocumentSnapshot readDocument = firestore.getDocumentFromReference(documentReference);

        //Assert
        try {
            DocumentSnapshot snapshot = firestore.getFirestore().collection(testCollection).document(readDocument.getId()).get().get();
            assertEquals(readDocument.get("name"),snapshot.get("name"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateDocument() {
        //Setup
        DocumentReference documentReference = firestore.addDocumentToNewCollection(testCollection, getBeanMap());
        usedCollections.add(testCollection);

        //Execute
        WriteResult result = firestore.updateDocumentField(testCollection, documentReference.getId(), "name", "updated name");

        //Assert
        assertEquals("updated name", firestore.getDocument(testCollection, documentReference.getId()).get("name"));
    }




}
