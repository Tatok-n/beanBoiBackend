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
    @Autowired
    BeanRepository beanRepository;


    @GetMapping("/users/{uid}/beans")
    public List<Map<String,Object>> getUserBeans(@PathVariable String uid) throws FileNotFoundException {
        return beanService.getAllBeansForUser(String.valueOf(uid)).stream().map(bean -> beanRepository.getAsMap(bean)).toList();
    }

    @GetMapping("/beans/{beanId}")
    public Map<String, Object> getBean(@PathVariable String beanId) throws FileNotFoundException {
        System.out.println(beanId);
        return beanRepository.getAsMap(beanService.getBeanById(beanId));
    }

    @PostMapping("/users/{userId}/beans/")
    public void addBean(@RequestBody Map<String,Object> bean, @PathVariable String userId) throws FileNotFoundException {
        System.out.println(bean);
        beanService.addBeanToUser((Bean) beanRepository.getFromMap(bean),userId);
    }
}
