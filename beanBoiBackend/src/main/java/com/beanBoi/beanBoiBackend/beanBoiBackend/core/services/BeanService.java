package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class BeanService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BeanRepository beanRepository;


    public List<Bean> getAllBeansForUser(String userId) throws FileNotFoundException {
        if (userRepository.getUserById(userId) != null) {
            return userRepository.getUserById(userId).getBeansOwned();
        }
        throw new FileNotFoundException("User does not exist");
    }

    public Bean getBeanById(String id) throws FileNotFoundException {
        if (beanRepository.getBeanById(id) != null) {
            return beanRepository.getBeanById(id);
        }
        throw new FileNotFoundException("Bean does not exist");
    }

    public Bean addBeanToUser(Bean bean, String uid) throws FileNotFoundException {
        if (userRepository.getUserById(uid) != null) {
            DocumentReference beanRef = beanRepository.saveDocument(bean);
            userRepository.updateDocumentListWithField(uid,beanRef,"beansOwned");
            return beanRepository.getBeanById(uid);
        }
        throw new FileNotFoundException("User does not exist");
    }
}
