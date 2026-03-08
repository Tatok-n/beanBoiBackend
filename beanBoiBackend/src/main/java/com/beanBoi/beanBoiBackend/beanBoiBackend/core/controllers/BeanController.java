package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class BeanController {

    @Autowired
    BeanService beanService;


    @GetMapping("/users/{uid}/beans")
    public List<Bean> getUserBeans(@PathVariable String uid) throws FileNotFoundException {
        return beanService.getAllBeansForUser(String.valueOf(uid));
    }

    @GetMapping("/beans/{beanId}")
    public Bean getBean(@PathVariable String beanId) throws FileNotFoundException {
        return beanService.getBeanById(beanId);
    }

    @PostMapping("/users/{userId}/beans/")
    public String addBean(@RequestBody Bean bean, @PathVariable String userId) throws FileNotFoundException {
        return beanService.createNewBean(bean,userId).getId();
    }

    @PostMapping("/users/{userId}/beans/{beanId}")
    public void updateBean(@RequestBody Bean bean, @PathVariable String userId, @PathVariable String beanId) throws FileNotFoundException {
        beanService.updateBean(userId,bean, beanId);
    }

    @DeleteMapping("/users/{userId}/beans/{beanId}")
    public void deleteBean(@PathVariable String beanId, @PathVariable String userId) throws FileNotFoundException {
        beanService.deleteBean(beanId,userId);
    }
}
