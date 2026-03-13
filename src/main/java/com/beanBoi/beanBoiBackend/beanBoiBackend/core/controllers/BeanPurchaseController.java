package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.DTOs.BeanPurchaseRequest;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanPurchaseRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class BeanPurchaseController {

    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    private BeanPurchaseService beanPurchaseService;

    @PostMapping("/users/{userId}/beanPurchases")
    BeanPurchase purchaseNewBean(@PathVariable String userId, @RequestBody BeanPurchase beanPurchase) throws FileNotFoundException {
        return beanPurchaseService.purchaseBean(beanPurchase.getName(), beanPurchase.getBeansPurchased().getId(), beanPurchase.getPurchaseDate(), beanPurchase.getRoastDate(), beanPurchase.getAmountPurchased(), beanPurchase.getPricePaid(), userId);
    }

    @PutMapping("/users/{userId}/beanPurchases/{purchaseId}")
    BeanPurchase editPurchase(@PathVariable String userId, @PathVariable String purchaseId, @RequestBody BeanPurchase purchase) throws FileNotFoundException {
        return beanPurchaseService.editPurchase(purchaseId, purchase.getName(), purchase.getBeansPurchased().getId(), purchase.getPurchaseDate(), purchase.getRoastDate(), purchase.getAmountPurchased(), purchase.getPricePaid(), userId);
    }

    @GetMapping("users/{userId}/beanPurchases")
    List<BeanPurchase> getBeanPurchases(@PathVariable String userId) throws FileNotFoundException {
        return beanPurchaseService.getAllBeanPurchasesForUser(userId);
    }

    @DeleteMapping("users/{userId}/beanPurchases/{purchaseId}")
    void deleteBeanPurchase(@PathVariable String userId, @PathVariable String purchaseId, @RequestBody boolean isArchive) throws FileNotFoundException {
        beanPurchaseService.deleteBeanPurchase(userId, purchaseId, isArchive);
    }
}
