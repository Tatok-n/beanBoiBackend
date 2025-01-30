package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.DocumentData;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

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

    public WriteResult updateDocumentField(DocumentData data, String field, Object value) {
        Map<String, Object> map = getAsMap(data);
        if (getAsMap(data).containsKey(field)) {
            map.put(field, value);
        } else {
            throw new IllegalArgumentException("Field " + field + " not found");
        }
        return firestoreImplementation.updateDocumentField(collectionName,data.getId(),field,value);
    }

    public String getNewId() {
        return firestoreImplementation.getFirestore().collection(collectionName).document().getId();
    }
}
