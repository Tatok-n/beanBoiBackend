package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GrinderRepository extends MongoRepository<Grinder, String> {

}