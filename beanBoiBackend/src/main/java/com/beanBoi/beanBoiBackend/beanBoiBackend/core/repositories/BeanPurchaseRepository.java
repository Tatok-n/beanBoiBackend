package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BeanPurchaseRepository extends MongoRepository<BeanPurchase, String> {
    List<BeanPurchase> findByUidAndActiveTrue(String uid, boolean active);
    List<BeanPurchase> findByUidAndActiveTrueAndBeansPurchasedId(String uid, boolean active, String beanId);

}