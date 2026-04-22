package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.*;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.RecipeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("users/recipes")
    public List<Recipe> getUserRecipes(@AuthenticationPrincipal User user) throws FileNotFoundException {
        String uid = user.getId();
        return recipeService.getRecipesForUser(uid);
    }


    @GetMapping("users/recipes/active")
    public List<Recipe> getAllRecipesForUser(@AuthenticationPrincipal User user) throws FileNotFoundException {
        String uid = user.getId();
        return recipeService.getAllRecipesForUser(uid);
    }


    @PostMapping("users/recipes")
    public Recipe createRecipe(
            @AuthenticationPrincipal User user,
            @RequestBody Recipe recipeRequest, @RequestBody List<Map<String, Object>> waterMap, @RequestBody boolean isEspresso) throws FileNotFoundException {
        String uid = user.getId();
        return recipeService.createRecipe(waterMap, recipeRequest.getDescription(), recipeRequest.getTemperature(), recipeRequest.getRatio(),recipeRequest.getDuration(), recipeRequest.getName(), uid, isEspresso);
    }

    @PutMapping("users/recipes/{id}")
    public Recipe updateRecipe(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Recipe recipe) throws FileNotFoundException {
        String uid = user.getId();
        if (recipe instanceof EspressoRecipe) {
            return recipeService.updateEspressoRecipe((EspressoRecipe) recipe);
        } else {
            return recipeService.updateV60Recipe((V60Recipe) recipe);
        }
    }


    @DeleteMapping("users/recipes/{id}")
    public void deleteRecipe(@AuthenticationPrincipal User user, @PathVariable String id) throws FileNotFoundException {
        String uid = user.getId();
        recipeService.deactivateRecipe(id);
    }


    @GetMapping("recipes/{id}")
    public Recipe getRecipeById(@PathVariable String id) throws FileNotFoundException {
        return recipeService.findRecipeById(id);
    }

}
