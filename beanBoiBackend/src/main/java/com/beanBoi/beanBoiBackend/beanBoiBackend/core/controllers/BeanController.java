package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
