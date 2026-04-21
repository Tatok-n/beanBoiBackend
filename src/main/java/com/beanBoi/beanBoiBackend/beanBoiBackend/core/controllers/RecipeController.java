package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.EspressoRecipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Recipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.V60Recipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("users/{uid}/recipes")
    public List<Recipe> getUserRecipes(@PathVariable String uid) throws FileNotFoundException {
        return recipeService.getRecipesForUser(uid);
    }


    @GetMapping("users/{uid}/recipes/active")
    public List<Recipe> getAllRecipesForUser(@PathVariable String uid) {
       return recipeService.getAllRecipesForUser(uid);
    }


    @PostMapping("users/{uid}/recipes")
    public Recipe createRecipe(
            @PathVariable String uid,
            @RequestBody Recipe recipeRequest, @RequestBody List<Map<String, Object>> waterMap, @RequestBody boolean isEspresso) throws FileNotFoundException {
        return recipeService.createRecipe(waterMap, recipeRequest.getDescription(), recipeRequest.getTemperature(), recipeRequest.getRatio(),recipeRequest.getDuration(), recipeRequest.getName(), uid, isEspresso);
    }

    @PutMapping("users/{uid}/recipes")
    public Recipe updateRecipe(@RequestBody Recipe recipe, @PathVariable String uid) throws FileNotFoundException {
        if (recipe instanceof EspressoRecipe) {
            return recipeService.updateEspressoRecipe((EspressoRecipe) recipe);
        } else {
            return recipeService.updateV60Recipe((V60Recipe) recipe);
        }
    }


    @DeleteMapping("users/{uid}/recipes/{id}")
    public void deleteRecipe(@PathVariable String uid, @PathVariable String id) throws FileNotFoundException {
        recipeService.deactivateRecipe(id);
    }


    @GetMapping("recipes/{id}")
    public Recipe getRecipeById(@PathVariable String id) throws FileNotFoundException {
        return recipeService.findRecipeById(id);
    }

}