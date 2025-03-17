package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.DTOs.BeanPurchaseRequest;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanPurchaseRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BeanPurchaseController {

    @Autowired
    private BeanPurchaseRepository beanPurchaseRepository;
    @Autowired
    private BeanPurchaseService beanPurchaseService;

    @PostMapping("/users/{userId}/beanPurchases")
    Map<String, Object> purchaseNewBean(@PathVariable String userId, @RequestBody BeanPurchaseRequest request) {
        return beanPurchaseRepository.getAsMap(beanPurchaseService.purchaseBean(request.name(), request.beanId(), request.dateOfPurchase(), request.dateOfRoast(), request.amountPurchased(), request.pricePaid(), userId));
    }

    @GetMapping("users/{userId}/beanPurchases")
    List<Map<String, Object>> getBeanPurchases(@PathVariable String userId) {
        return beanPurchaseService.getAllBeanPurchasesForUser(userId);
    }
}
