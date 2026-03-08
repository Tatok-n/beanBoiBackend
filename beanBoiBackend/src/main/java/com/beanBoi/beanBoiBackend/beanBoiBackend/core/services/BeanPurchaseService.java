package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanPurchaseRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeanPurchaseService {

    private final BeanRepository beanRepository;
    private final BeanPurchaseRepository beanPurchaseRepository;
    private final UserRepository userRepository;


    public BeanPurchase purchaseBean(String name, String beanId, LocalDate dateOfPurchase,
                                     LocalDate dateOfRoast, float amountPurchased, float pricePaid, String uid) throws FileNotFoundException {

        // Verify user exists
        if (!userRepository.existsById(uid)) {
            throw new FileNotFoundException("User does not exist");
        }

        // Update bean stats
        Bean bean = beanRepository.findById(beanId)
                .orElseThrow(() -> new FileNotFoundException("Bean does not exist"));
        bean.setTimesPurchased(bean.getTimesPurchased() + 1);
        beanRepository.save(bean);

        BeanPurchase purchase = new BeanPurchase();
        purchase.setBeansPurchased(bean);
        purchase.setRoastDate(dateOfRoast);
        purchase.setPurchaseDate(dateOfPurchase);
        purchase.setAmountPurchased(amountPurchased);
        purchase.setAmountRemaining(amountPurchased);
        purchase.setActive(true);
        purchase.setUid(uid);
        purchase.setPricePaid(pricePaid);
        purchase.setName(name.isBlank() ? bean.getName() + "-" + bean.getRoaster() : name);

        BeanPurchase saved = beanPurchaseRepository.save(purchase);
        computeNewAveragePrice(beanId, uid);
        return saved;
    }

    public BeanPurchase editQuantity(String purchaseId, float amount) throws FileNotFoundException {
        BeanPurchase purchase = beanPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new FileNotFoundException("Purchase does not exist"));

        float remaining = purchase.getAmountRemaining();
        if (remaining <= amount) {
            remaining = 0;
        } else if (remaining + amount > purchase.getAmountPurchased()) {
            purchase.setAmountPurchased(remaining + amount);
            remaining += amount;
        } else {
            remaining += amount;
        }

        purchase.setAmountRemaining(remaining);
        return beanPurchaseRepository.save(purchase);
    }

    public void editRemainingAmount(String purchaseId, float amount) throws FileNotFoundException {
        BeanPurchase purchase = beanPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new FileNotFoundException("Purchase not found"));
        purchase.setAmountRemaining(amount);
        beanPurchaseRepository.save(purchase);
    }


    public void deleteBeanPurchase(String purchaseId, String uid, boolean isArchive) throws FileNotFoundException {
        BeanPurchase purchase = beanPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new FileNotFoundException("Purchase not found"));


        if (!isArchive) {
            // Decrement bean stats
            Bean bean = beanRepository.findById(purchase.getBeansPurchased().getId())
                    .orElseThrow(() -> new FileNotFoundException("Bean not found"));
            bean.setTimesPurchased(Math.max(0, bean.getTimesPurchased() - 1));
            beanRepository.save(bean);
            computeNewAveragePrice(bean.getId(), uid);
        }

        // Soft delete
        purchase.setActive(false);
        beanPurchaseRepository.save(purchase);
    }


    private void computeNewAveragePrice(String beanId, String uid) {
        List<BeanPurchase> purchases = beanPurchaseRepository
                .findByUidAndActiveTrueAndBeansPurchasedId(uid, true, beanId);

        if (purchases.isEmpty()) return;

        float totalWeight = purchases.stream()
                .map(BeanPurchase::getAmountPurchased)
                .reduce(0f, Float::sum);

        float totalPrice = purchases.stream()
                .map(p -> p.getAmountPurchased() * p.getPricePaid())
                .reduce(0f, Float::sum);

        if (totalWeight > 0) {
            float avgPrice = totalPrice / totalWeight;
            Bean bean = beanRepository.findById(beanId).orElseThrow();
            bean.setPrice(avgPrice);
            beanRepository.save(bean);
        }
    }

    /**
     * Edit complete purchase details
     */
    public BeanPurchase editPurchase(String purchaseId, String name, String beanId,
                                     LocalDate newDateOfPurchase, LocalDate newDateOfRoast,
                                     float amountPurchased, float newPrice, String uid) throws FileNotFoundException {

        BeanPurchase purchase = beanPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new FileNotFoundException("Purchase not found"));
        Bean bean = beanRepository.findById(beanId)
                .orElseThrow(() -> new FileNotFoundException("Bean not found"));

        if (!purchase.getUid().equals(uid)) {
            throw new FileNotFoundException("Not authorized");
        }

        // Recalculate before edit
        computeNewAveragePrice(beanId, uid);

        // Update purchase
        purchase.setBeansPurchased(bean);
        purchase.setRoastDate(newDateOfRoast);
        purchase.setPurchaseDate(newDateOfPurchase);
        purchase.setAmountPurchased(amountPurchased);
        purchase.setPricePaid(newPrice);
        purchase.setName(name.isBlank() ? bean.getName() + "-" + bean.getRoaster() : name);

        return beanPurchaseRepository.save(purchase);
    }

    /**
     * Get all active purchases for user (pure uid query)
     */
    public List<BeanPurchase> getAllBeanPurchasesForUser(String uid) throws FileNotFoundException {
        if (!userRepository.existsById(uid)) {
            throw new FileNotFoundException("User does not exist");
        }
        return beanPurchaseRepository.findByUidAndActiveTrue(uid, true);
    }
}
