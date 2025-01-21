package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class testFirestoreImplementation extends TestUtils{
    @Autowired
    FirestoreImplementation firestoreImplementation;
    @Autowired
    private BeanRepository beanRepository;


    @Test
    public void testAddToANonExistentCollection() {
        //Execute
        Exception exception = assertThrows(FileNotFoundException.class,() -> firestoreImplementation.addDocumentToCollection("FAKE COLLECTION", new HashMap<>()));

        //Assert
        assertEquals("Collection FAKE COLLECTION does not exist!", exception.getMessage());
    }


    @Test
    public void testAddToCollection() {
        //Execute
        DocumentReference documentReference = firestoreImplementation.addDocumentToNewCollection(testCollection, getBeanMap());
        HashMap<String, Object> beanmap = getBeanMap();
        beanmap.put("id", documentReference.getId());
        addedDocuments.add(documentReference);

        //Assert
        assertEquals(documentReference,firestoreImplementation.getFirestore().collection(testCollection).document(beanmap.get("id").toString()));
    }

    @Test
    public void testGetDocument() {
        //Setup
        DocumentReference documentReference = firestoreImplementation.addDocumentToNewCollection(testCollection, getBeanMap());
        addedDocuments.add(documentReference);

        //Execute
        DocumentSnapshot readDocument = firestoreImplementation.getDocumentFromReference(documentReference);

        //Assert
        try {
            DocumentSnapshot snapshot = firestoreImplementation.getFirestore().collection(testCollection).document(readDocument.getId()).get().get();
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
        DocumentReference documentReference = firestoreImplementation.addDocumentToNewCollection(testCollection, getBeanMap());
        addedDocuments.add(documentReference);

        //Execute
        WriteResult result = firestoreImplementation.updateDocumentField(beanRepository.collectionName, documentReference.getId(), "name", "updated name");

        //Assert
        assertEquals("updated name", firestoreImplementation.getDocument(beanRepository.collectionName, documentReference.getId()).get("name"));
    }




}
