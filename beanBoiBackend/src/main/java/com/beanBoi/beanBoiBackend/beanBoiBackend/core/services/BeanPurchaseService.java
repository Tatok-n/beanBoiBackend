package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanPurchaseRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BeanPurchaseService {

    @Autowired
    BeanRepository beanRepository;

    DecimalFormat format = new DecimalFormat("#.00");
    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    private UserRepository userRepository;


    public BeanPurchase purchaseBean(String name, String beanId, LocalDate dateOfPurchase, LocalDate dateOfRoast, float amountPurchased, float pricePaid,String uid) {
        BeanPurchase beanPurchase = new BeanPurchase();
        float oldPrice = (float) beanRepository.getDocumentField(beanId, "price");
        int timesPurchased = (int) beanRepository.getDocumentField(beanId, "timesPurchased") + 1;
        beanRepository.updateDocumentField(beanId, "timesPurchased", timesPurchased);

        float newPrice = ((pricePaid/amountPurchased) + oldPrice) / timesPurchased;
        newPrice =  Float.parseFloat(format.format(String.valueOf(newPrice)));
        beanRepository.updateDocumentField(beanId, "price", timesPurchased);

        beanPurchase.setBeansPurchased(beanRepository.getBeanById(beanId));
        beanPurchase.setRoastDate(dateOfRoast);
        beanPurchase.setPurchaseDate(dateOfPurchase);
        beanPurchase.setAmountPurchased(amountPurchased);
        beanPurchase.setAmountRemaining(amountPurchased);
        beanPurchase.setAmountRemaining(amountPurchased);
        beanPurchase.setActive(true);
        beanPurchase.setUid(uid);
        beanPurchase.setName(name.isBlank() ? beanRepository.getDocumentField(beanId,"name") + "-" + beanRepository.getDocumentField(beanId,"roaster") : name);

        DocumentReference beanPurchaseRef = beanPurchaseRepository.saveDocument(beanPurchase);
        beanPurchase.setId(beanPurchaseRef.getId());
        userRepository.updateDocumentListWithField(uid,beanPurchaseRef,"beansAvailable");
        return beanPurchase;

    }

    public List<Map<String, Object>> getAllBeanPurchasesForUser(String uid) {
        return beanPurchaseRepository.getActiveBeanPurchaseForUser(uid).stream().map(purchase -> beanPurchaseRepository.getAsMap(purchase)).toList();
    }
}
