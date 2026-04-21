package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.BeanService;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class BeanController {

    @Autowired
    BeanService beanService;

    @Autowired
    UserService userService;


    @GetMapping("/beans")
    public List<Bean> getUserBeans(@AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        return beanService.getAllBeansForUser(userService.getFromGoogleId(jwt).getId());
    }

    @GetMapping("/beans/{beanId}")
    public Bean getBean(@PathVariable String beanId) throws FileNotFoundException {
        return beanService.getBeanById(beanId);
    }

    @PostMapping("/users/beans/")
    public String addBean(@RequestBody Bean bean, @AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        String userId = userService.getFromGoogleId(jwt).getId();
        return beanService.createNewBean(bean,userId).getId();
    }

    @PostMapping("/users/beans/{beanId}")
    public void updateBean(@RequestBody Bean bean, @PathVariable String beanId, @AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        String userId = userService.getFromGoogleId(jwt).getId();
        beanService.updateBean(userId,bean, beanId);
    }

    @DeleteMapping("/users/beans/{beanId}")
    public void deleteBean(@PathVariable String beanId, @AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        String userId = userService.getFromGoogleId(jwt).getId();
        beanService.deleteBean(beanId,userId);
    }
}
