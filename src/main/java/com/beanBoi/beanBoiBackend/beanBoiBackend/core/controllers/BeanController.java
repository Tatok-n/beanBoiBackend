package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class BeanController {

    @Autowired
    BeanService beanService;

    @GetMapping("/beans")
    public List<Bean> getUserBeans(@AuthenticationPrincipal User user) throws FileNotFoundException {
        return beanService.getAllBeansForUser(user.getId());
    }

    @GetMapping("/beans/{beanId}")
    public Bean getBean(@PathVariable String beanId) throws FileNotFoundException {
        return beanService.getBeanById(beanId);
    }

    @PostMapping("/users/beans/")
    public String addBean(@RequestBody Bean bean, @AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        return beanService.createNewBean(bean, userId).getId();
    }

    @PostMapping("/users/beans/{beanId}")
    public void updateBean(
            @RequestBody Bean bean,
            @PathVariable String beanId,
            @AuthenticationPrincipal User user
    ) throws FileNotFoundException {
        String userId = user.getId();
        beanService.updateBean(userId, bean, beanId);
    }

    @DeleteMapping("/users/beans/{beanId}")
    public void deleteBean(@PathVariable String beanId, @AuthenticationPrincipal User user) throws FileNotFoundException {
        String userId = user.getId();
        beanService.deleteBean(beanId, userId);
    }
}
