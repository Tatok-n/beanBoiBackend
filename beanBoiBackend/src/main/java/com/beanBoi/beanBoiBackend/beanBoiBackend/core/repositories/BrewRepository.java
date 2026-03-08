package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Brew;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrewRepository extends MongoRepository<Brew, String> {

}