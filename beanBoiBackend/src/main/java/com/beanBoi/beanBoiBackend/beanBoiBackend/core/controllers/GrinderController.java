package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Grinder;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.GrinderRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.GrinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GrinderController {

    @Autowired
    GrinderService grinderService;
    @Autowired
    GrinderRepository grinderRepository;



    @GetMapping("users/{uid}/grinders")
    public List<Map<String, Object>> getGrindersForUser(@PathVariable String uid) {
        return grinderService.getGrindersForUser(uid);
    }

    @PutMapping("users/{uid}/grinders")
    public Map<String, Object> addGrinder(@PathVariable String uid, @RequestBody Map<String, Object> map) {
        Grinder newGrinder = grinderService.addGrinderToUser(map, uid);
        return grinderRepository.getAsMap(newGrinder);
    }

    @PostMapping("users/{uid}/grinders/{id}")
    public Map<String, Object> updateGrinder(@PathVariable String uid, @PathVariable String id, @RequestBody Map<String, Object> map) {
        Grinder newGrinder = grinderService.editGrinder(map,id, uid);
        return grinderRepository.getAsMap(newGrinder);
    }

    @DeleteMapping("users/{uid}/grinders/{id}")
    public void deleteGrinder(@PathVariable String uid, @PathVariable String id) {
        grinderService.deleteGrinder(id);
    }
}
