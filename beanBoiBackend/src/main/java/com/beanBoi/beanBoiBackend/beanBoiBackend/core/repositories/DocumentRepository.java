package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public abstract class DocumentRepository {

    @Autowired
    private FirestoreImplementation firestoreImplementation;
    public String collectionName;

    public abstract Map<String, Object> getAsMap(DocumentData data);
    public abstract DocumentData getFromMap(Map<String, Object> map);


    public WriteResult deleteDocument(DocumentData data) {
        return firestoreImplementation.deleteDocument(collectionName, data.getId());
    }

    public DocumentData getDocumentById(String id) {
        DocumentSnapshot document = firestoreImplementation.getDocument(collectionName,id);
        Map<String, Object> dataMap = document.getData();
        dataMap.put("id", id);
        DocumentData data = getFromMap(dataMap);
        return data;
    }

    public abstract DocumentReference saveDocument(DocumentData data);

    public DocumentReference saveDocumentWithId(String id, DocumentData data) {
        DocumentReference documentReference = firestoreImplementation.addDocumentToCollectionWithId(collectionName,getAsMap(data),id);
        return documentReference;
    }

    public WriteResult updateDocument(DocumentData data) {
        return firestoreImplementation.updateDocument(collectionName,data.getId(),getAsMap(data));
    }

    public WriteResult updateDocumentListWithField(String id, Object data, String fieldName ) {
        DocumentReference documentReference = firestoreImplementation.getDocumentReference(collectionName,id);
        ApiFuture<WriteResult> arrayUnion = documentReference.update(fieldName, FieldValue.arrayUnion(data));
        try {
            return arrayUnion.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getDocumentField(String id, String fieldName) {
        DocumentReference documentReference = firestoreImplementation.getDocumentReference(collectionName,id);
        DocumentSnapshot document;
        try {
            document = documentReference.get().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return document.get(fieldName);
    }

    public WriteResult updateDocumentField(String documentId, String field, Object value) {
        return firestoreImplementation.updateDocumentField(collectionName,documentId,field,value);
    }

    public String getNewId() {
        return firestoreImplementation.getFirestore().collection(collectionName).document().getId();
    }


}
