package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.GrinderRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.GrinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
public class GrinderController {

    @Autowired
    GrinderService grinderService;
    @Autowired
    GrinderRepository grinderRepository;
    @Autowired
    private UserService userService;



    @GetMapping("users/grinders")
    public List<Grinder> getGrindersForUser(@AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        return grinderService.getGrindersForUser(uid);
    }

    @PutMapping("users/grinders")
    public Grinder addGrinder(@AuthenticationPrincipal Jwt jwt, @RequestBody Grinder grinder) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        return grinderService.addGrinderToUser(grinder, uid);
    }

    @PostMapping("users/grinders/{id}")
    public Grinder updateGrinder(@AuthenticationPrincipal Jwt jwt, @PathVariable String id, @RequestBody Grinder grinder) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        return  grinderService.editGrinder(grinder,id, uid);
    }

    @DeleteMapping("users/grinders/{id}")
    public void deleteGrinder(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        String uid = userService.getFromGoogleId(jwt).getId();
        grinderService.deleteGrinder(id, uid);
    }
}
