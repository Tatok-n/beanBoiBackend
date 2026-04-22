package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.GrinderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class GrinderController {

    @Autowired
    GrinderService grinderService;



    @GetMapping("users/grinders")
    public List<Grinder> getGrindersForUser(@AuthenticationPrincipal User user) throws FileNotFoundException {
        String uid = user.getId();
        return grinderService.getGrindersForUser(uid);
    }

    @PutMapping("users/grinders")
    public Grinder addGrinder(@AuthenticationPrincipal User user, @RequestBody Grinder grinder) throws FileNotFoundException {
        String uid = user.getId();
        return grinderService.addGrinderToUser(grinder, uid);
    }

    @PostMapping("users/grinders/{id}")
    public Grinder updateGrinder(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Grinder grinder) throws FileNotFoundException {
        String uid = user.getId();
        return  grinderService.editGrinder(grinder,id, uid);
    }

    @DeleteMapping("users/grinders/{id}")
    public void deleteGrinder(@AuthenticationPrincipal User user, @PathVariable String id) throws FileNotFoundException {
        String uid = user.getId();
        grinderService.deleteGrinder(id);
    }
}
