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

    @Autowired
    private UserService userService;

    @GetMapping("users/recipes")
    public List<Recipe> getUserRecipes(@AuthenticationPrincipal Jwt jwt) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        return recipeService.getRecipesForUser(uid);
    }


    @GetMapping("users/recipes/active")
    public List<Recipe> getAllRecipesForUser(@AuthenticationPrincipal Jwt jwt) {
        String uid = userService.getFromGoogleId(jwt).getId();
        return recipeService.getAllRecipesForUser(uid);
    }


    @PostMapping("users/recipes")
    public Recipe createRecipe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Recipe recipeRequest, @RequestBody List<Map<String, Object>> waterMap, @RequestBody boolean isEspresso) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        return recipeService.createRecipe(waterMap, recipeRequest.getDescription(), recipeRequest.getTemperature(), recipeRequest.getRatio(),recipeRequest.getDuration(), recipeRequest.getName(), uid, isEspresso);
    }

    @PutMapping("users/recipes/{id}")
    public Recipe updateRecipe(@AuthenticationPrincipal Jwt jwt, @PathVariable String id, @RequestBody Recipe recipe) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        if (recipe instanceof EspressoRecipe) {
            return recipeService.updateEspressoRecipe((EspressoRecipe) recipe);
        } else {
            return recipeService.updateV60Recipe((V60Recipe) recipe);
        }
    }


    @DeleteMapping("users/recipes/{id}")
    public void deleteRecipe(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) throws FileNotFoundException {
        String uid = userService.getFromGoogleId(jwt).getId();
        recipeService.deactivateRecipe(id, uid  );
    }


    @GetMapping("recipes/{id}")
    public Recipe getRecipeById(@PathVariable String id) throws FileNotFoundException {
        return recipeService.findRecipeById(id);
    }

}
