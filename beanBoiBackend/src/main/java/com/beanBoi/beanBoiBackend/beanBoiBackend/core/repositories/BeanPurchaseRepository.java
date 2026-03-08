package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.BeanPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BeanPurchaseRepository extends MongoRepository<BeanPurchase, String> {

}