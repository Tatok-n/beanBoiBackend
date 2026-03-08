package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class BeanService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BeanRepository beanRepository;


    public List<Bean> getAllBeansForUser(String userId) throws FileNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            return beanRepository.findAllByUid(userId);
        }
        throw new FileNotFoundException("User does not exist");
    }

    public Bean getBeanById(String id) throws FileNotFoundException {
        if (beanRepository.findById(id).isPresent()) {
            return beanRepository.findById(id).get();
        }
        throw new FileNotFoundException("Bean does not exist");
    }

    public void updateBean(String uid, Bean bean, String beanId) throws FileNotFoundException {
        bean.setUid(uid);
        bean.setId(beanId);
        beanRepository.save(bean);
    }

    public Bean addBeanToUser(Bean bean, String uid) throws FileNotFoundException {
        // TODO : Look into switching to simply passing bean ID
        if (userRepository.findById(uid).isPresent() && beanRepository.findById(bean.getId()).isPresent()) {
            bean.setUid(uid);
            return beanRepository.save(bean);
        }
        throw new FileNotFoundException("User does not exist");
    }

    public Bean createNewBean(Bean bean, String uid) throws FileNotFoundException {
        if (userRepository.findById(uid).isPresent()) {
            bean.setUid(uid);
            return beanRepository.save(bean);
        }
        throw new FileNotFoundException("User does not exist");
    }

    public void deleteBean(String beanId, String UID) throws FileNotFoundException {
        if (beanRepository.findById(beanId).isPresent() && userRepository.findById(UID).isPresent()) {
            User user = userRepository.findById(UID).get();
            Bean bean = beanRepository.findById(beanId).get();
            bean.setActive(false);
            beanRepository.save(bean);
            user.getBeansOwned().remove(bean);
            userRepository.save(user);
        }
        throw new FileNotFoundException("User or bean does not exist");

    }
}
