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



    @GetMapping("users/{uid}/grinders")
    public List<Grinder> getGrindersForUser(@PathVariable String uid) throws FileNotFoundException {
        return grinderService.getGrindersForUser(uid);
    }

    @PutMapping("users/{uid}/grinders")
    public Grinder addGrinder(@PathVariable String uid, @RequestBody Grinder grinder) throws FileNotFoundException {
        return grinderService.addGrinderToUser(grinder, uid);
    }

    @PostMapping("users/{uid}/grinders/{id}")
    public Grinder updateGrinder(@PathVariable String uid, @PathVariable String id, @RequestBody Grinder grinder) throws FileNotFoundException {
        return  grinderService.editGrinder(grinder,id, uid);
    }

    @DeleteMapping("users/{uid}/grinders/{id}")
    public void deleteGrinder(@PathVariable String uid, @PathVariable String id) {
        grinderService.deleteGrinder(id);
    }
}
