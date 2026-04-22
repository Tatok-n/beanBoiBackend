package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanPurchaseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class BeanPurchaseController {
    @Autowired
    private BeanPurchaseService beanPurchaseService;

    @PostMapping("/users/beanPurchases")
    BeanPurchase purchaseNewBean(@RequestBody BeanPurchase beanPurchase, @AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        return beanPurchaseService.purchaseBean(beanPurchase.getName(), beanPurchase.getBeansPurchased().getId(), beanPurchase.getPurchaseDate(), beanPurchase.getRoastDate(), beanPurchase.getAmountPurchased(), beanPurchase.getPricePaid(), userId);
    }

    @PutMapping("/users/beanPurchases/{purchaseId}")
    BeanPurchase editPurchase(@PathVariable String purchaseId, @RequestBody BeanPurchase purchase, @AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        return beanPurchaseService.editPurchase(purchaseId, purchase.getName(), purchase.getBeansPurchased().getId(), purchase.getPurchaseDate(), purchase.getRoastDate(), purchase.getAmountPurchased(), purchase.getPricePaid(), userId);
    }

    @GetMapping("users/beanPurchases")
    List<BeanPurchase> getBeanPurchases(@AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        return beanPurchaseService.getAllBeanPurchasesForUser(userId);
    }

    @DeleteMapping("users/beanPurchases/{purchaseId}")
    void deleteBeanPurchase(@PathVariable String purchaseId, @RequestBody boolean isArchive, @AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        beanPurchaseService.deleteBeanPurchase(userId, purchaseId, isArchive);
    }
}
