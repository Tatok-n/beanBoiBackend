package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BeanRepository extends MongoRepository<Bean, String> {
    List<Bean> findAllByUid(String uid);

}
