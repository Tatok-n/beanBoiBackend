package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.*;
import com.beanBoi.beanBoiBackend.beanBoiBackend.firestore.repositories.FirestoreImplementation;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class BrewRepository extends DocumentRepository {


    private final BeanPurchaseRepository beanPurchaseRepository;
    private final GrinderRepository grinderRepository;
    private final FirestoreImplementation firestoreImplementation;

    public BrewRepository(BeanPurchaseRepository beanPurchaseRepository, GrinderRepository grinderRepository, FirestoreImplementation firestoreImplementation) {
        this.collectionName = "brews";
        this.beanPurchaseRepository = beanPurchaseRepository;
        this.grinderRepository = grinderRepository;
        this.firestoreImplementation = firestoreImplementation;
    }

    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        Brew brew = (Brew) data;
        Map<String, Object> brewMap = new HashMap<String, Object>();
        brewMap.put("createdAt",brew.getBrewDate());
        brewMap.put("coffeeUsed",beanPurchaseRepository.getAsMap(brew.getCoffeeUsed()));
        brewMap.put("grinderUsed",grinderRepository.getAsMap(brew.getGrinderUsed()));

        brewMap.put("doseIn",String.valueOf(brew.getDoseIn()));
        brewMap.put("doseOut",String.valueOf(brew.getDoseOut()));
        brewMap.put("temperature",String.valueOf(brew.getTemperature()));
        brewMap.put("duration",String.valueOf(brew.getDuration()));

        brewMap.put("type", String.valueOf(brew.getBrewType()));
        brewMap.put("isActive", brew.isActive());
        brewMap.put("grindSetting", brew.getGrindSetting());
        brewMap.put("notes", brew.getNotes());
        brewMap.put("uid", brew.getUid());
        brewMap.put("id", brew.getId());
        return brewMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {
        Brew brew = new Brew();
        brew.setBrewDate((Timestamp) map.get("createdAt"));

        brew.setDoseIn(Float.parseFloat(map.get("doseIn").toString()));
        brew.setDoseOut(Float.parseFloat(map.get("doseOut").toString()));
        brew.setDuration(Float.parseFloat(map.get("duration").toString()));
        brew.setTemperature(Float.parseFloat(map.get("temperature").toString()));
        brew.setUid((String) map.get("uid"));

        brew.setNotes(map.get("notes").toString());
        brew.setGrindSetting(map.get("grindSetting").toString());
        brew.setId(map.get("id").toString());
        if (map.get("type").equals("Espresso")) {
            brew.setBrewType(BrewType.Espresso);
        } else {
            brew.setBrewType(BrewType.V60);
        }
        brew.setActive(Boolean.valueOf(map.get("isActive").toString()));

        Grinder usedGrinder = grinderRepository.verifyGrinder((DocumentReference)map.get("grinderUsed"));
        brew.setGrinderUsed(usedGrinder);

        BeanPurchase usedBeans = beanPurchaseRepository.verifyPurchase((DocumentReference)map.get("coffeeUsed"));
        brew.setCoffeeUsed(usedBeans);
        return brew;
    }

    @Override
    public DocumentReference saveDocument(DocumentData data) {
        if (data.getId() == null) {
            data.setId(getNewId());
        }

        DocumentReference usedBeans = beanPurchaseRepository.saveDocument(((Brew) data).getCoffeeUsed());
        DocumentReference grinderUsed = grinderRepository.saveDocument(((Brew) data).getGrinderUsed());
        Map<String, Object> brewMap = getAsMap(data);
        brewMap.put("grinderUsed", grinderUsed);
        brewMap.put("coffeeUsed", usedBeans);
        return firestoreImplementation.addDocumentToCollectionWithId(collectionName, brewMap, data.getId());

    }

    public Brew verifyBrew(DocumentReference reference) {
        Brew brew = new Brew();
        if (firestoreImplementation.getDocumentFromReference(reference).getData() == null) {
            brew.setDuration(-1f);
        } else {
            brew = getBrewById(firestoreImplementation.getDocumentFromReference(reference).getId());
        }
        return brew;
    }







    public Brew getBrewById(String id) {
        return (Brew) getDocumentById(id);
    }
}
