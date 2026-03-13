package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GrinderRepository extends MongoRepository<Grinder, String> {
    List<Grinder> findGrindersByUid(String uid);

}