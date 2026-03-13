package com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}