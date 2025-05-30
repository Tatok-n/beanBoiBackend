package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanPurchaseRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
        float oldPrice = Float.parseFloat(beanRepository.getDocumentField(beanId, "price").toString());
        int timesPurchased = Integer.parseInt(beanRepository.getDocumentField(beanId, "timesPurchased").toString())  + 1;
        beanRepository.updateDocumentField(beanId, "timesPurchased", timesPurchased);


        beanPurchase.setBeansPurchased(beanRepository.getBeanById(beanId));
        beanPurchase.setRoastDate(dateOfRoast);
        beanPurchase.setPurchaseDate(dateOfPurchase);
        beanPurchase.setAmountPurchased(amountPurchased);
        beanPurchase.setAmountRemaining(amountPurchased);
        beanPurchase.setActive(true);
        beanPurchase.setUid(uid);
        beanPurchase.setName(name.isBlank() ? beanRepository.getDocumentField(beanId,"name") + "-" + beanRepository.getDocumentField(beanId,"roaster") : name);

        DocumentReference beanPurchaseRef = beanPurchaseRepository.saveDocument(beanPurchase);
        beanPurchase.setId(beanPurchaseRef.getId());
        userRepository.updateDocumentListWithField(uid,beanPurchaseRef,"beansAvailable");
        computeNewAveragePrice(beanId,uid);
        return beanPurchase;
    }

    public BeanPurchase editQuantity(String purchaseId, float amount) {
        BeanPurchase beanPurchase = beanPurchaseRepository.getBeanPurchaseById(purchaseId);
        float amountRemaining = beanPurchase.getAmountRemaining();
        if (amountRemaining <= amount) {
            amountRemaining = 0;
        } else if (amountRemaining + amount > beanPurchase.getAmountPurchased()) {
            amountRemaining += amount;
            beanPurchaseRepository.updateDocumentField(beanPurchase.getId(), "amountPurchased", amountRemaining);
        } else {
            amountRemaining += amount;
        }
        beanPurchaseRepository.updateDocumentField(beanPurchase.getId(), "amountRemaining", amountRemaining);
        return beanPurchase;
    }

    public void editRemainingAmount(String purchaseId, float amount) {
        beanPurchaseRepository.updateDocumentField(purchaseId, "amountRemaining", amount);
    }

    public void deleteBeanPurchase(String purchaseId, String uid, boolean isArchive) {
        if (!isArchive) {
            BeanPurchase beanPurchase = beanPurchaseRepository.getBeanPurchaseById(purchaseId);
            Bean bean = beanRepository.getBeanById(beanPurchase.getBeansPurchased().getId());
            beanRepository.updateDocumentField(bean.getId(), "timesPurchased", bean.getTimesPurchased() - 1); //eventually actually compute new average ?
            computeNewAveragePrice(bean.getId(),uid);
        }

        beanPurchaseRepository.updateDocumentField(purchaseId, "isActive", false);
        List<DocumentReference> beanRefs = new ArrayList<>(((List<DocumentReference>) userRepository.getDocumentField(uid, "beansAvailable")));
        DocumentReference beanToRemove = beanRefs.stream()
                .filter(doc -> doc.getId().equals(purchaseId))
                .findFirst().orElse(null);
        if (beanToRemove != null) {
            beanRefs.remove(beanToRemove);
            userRepository.updateDocumentField(uid,"beansAvailable",beanRefs);
        }

    }

    public void computeNewAveragePrice(String beanId, String uid) {
        List<BeanPurchase> beanPurchases = beanPurchaseRepository.getActiveBeanPurchaseForUser(uid).stream()
                .filter(beanPurchase -> beanPurchase.getBeansPurchased().getId().equals(beanId)).toList();

        float totalWeight = beanPurchases.stream().map(BeanPurchase::getAmountPurchased).reduce(0f, Float::sum);
        float totalWeightedPrice = beanPurchases.stream().map(beanPurchase -> beanPurchase.getAmountPurchased() * beanPurchase.getPricePaid()).reduce(0f, Float::sum);
        float averagePrice = totalWeightedPrice / totalWeight;

        beanRepository.updateDocumentField(beanId, "price", averagePrice);
    }

    public BeanPurchase editPurchase(String purchaseId, String name, String beanId, LocalDate newDateOfPurchase, LocalDate newDateOfRoast, float amountPurchased, float newPrice,String uid) {
        BeanPurchase beanPurchase = beanPurchaseRepository.getBeanPurchaseById(purchaseId);
        computeNewAveragePrice(beanId,uid);

        beanPurchase.setBeansPurchased(beanRepository.getBeanById(beanId));
        beanPurchase.setRoastDate(newDateOfRoast);
        beanPurchase.setPurchaseDate(newDateOfPurchase);
        beanPurchase.setAmountPurchased(amountPurchased);
        beanPurchase.setAmountRemaining(beanPurchase.getAmountRemaining());
        beanPurchase.setActive(true);
        beanPurchase.setUid(uid);
        beanPurchase.setName(name.isBlank() ? beanRepository.getDocumentField(beanId,"name") + "-" + beanRepository.getDocumentField(beanId,"roaster") : name);

        DocumentReference beanPurchaseRef = beanPurchaseRepository.saveDocument(beanPurchase);
        return beanPurchase;
    }

    public List<Map<String, Object>> getAllBeanPurchasesForUser(String uid) {
        return beanPurchaseRepository.getActiveBeanPurchaseForUser(uid).stream().map(purchase -> beanPurchaseRepository.getAsMap(purchase)).toList();
    }
}
