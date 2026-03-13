package com.beanBoi.beanBoiBackend.beanBoiBackend.core.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.EspressoRecipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Recipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.V60Recipe;
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

/**
 * Controller for recipe management operations.
 * Provides CRUD endpoints for creating, reading, updating, and deleting recipes.
 * Supports both Espresso and V60 recipe types.
 */
@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * Get all recipes owned by a specific user.
     * Returns list of Map<String, Object> representing recipe documents.
     * 
     * @param uid The unique identifier for the user
     * @return List of recipe data maps if found, empty list otherwise
     * @throws FileNotFoundException if user has no recipes
     */
    @GetMapping("users/{uid}/recipes")
    public List<Map<String, Object>> getUserRecipes(@PathVariable String uid) throws FileNotFoundException {
        List<Recipe> recipes = getRecipesByUser(uid);
        return recipes.stream()
                .map(recipe -> convertToMap(recipe))
                .toList();
    }

    /**
     * Get all active recipes for a user (where isActive is true).
     * 
     * @param uid The unique identifier for the user
     * @return List of recipe data maps filtered by active status
     */
    @GetMapping("users/{uid}/recipes/active")
    public List<Map<String, Object>> getActiveUserRecipes(@PathVariable String uid) {
        return getUserRecipes(uid).stream()
                .filter(map -> map.get("isActive", Boolean.class) != null && 
                              ((Boolean) map.get("isActive")).booleanValue())
                .toList();
    }

    /**
     * Create a new recipe (supports Espresso and V60 types).
     * Accepts recipe data including water flow configuration.
     * 
     * @param uid The unique identifier for the user owning the recipe
     * @param requestBody Recipe creation data including:
     *                   - name, description, temperature, ratio, duration
     *                   - waterMap (list of water stages with time/flow amounts)
     *                   - isEspresso flag to determine recipe type
     * @return ResponseEntity with created recipe data
     */
    @PostMapping("users/{uid}/recipes")
    public ResponseEntity<Map<String, Object>> createRecipe(
            @PathVariable String uid, 
            @RequestBody Recipe recipeRequest) throws FileNotFoundException {
        
        // Validate required fields
        if (recipeRequest.getName() == null || recipeRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Recipe name is required");
        }
        
        // Check if user has existing recipes
        List<Map<String, Object>> existingRecipes = getUserRecipes(uid);
        if (!existingRecipes.isEmpty()) {
            // Optional: check for duplicate names or throw exception
            for (Map<String, Object> recipe : existingRecipes) {
                if ("name".equals(recipe.get()) && 
                    recipeRequest.getName().equals(recipe.get("name"))) {
                    throw new IllegalArgumentException("A recipe with name '" + recipeRequest.getName() + 
                            "' already exists for this user");
                }
            }
        }
        
        // Create recipe using service
        Recipe createdRecipe = recipeService.createRecipe(
                extractWaterMap(recipeRequest.getWater()),
                recipeRequest.getDescription(),
                recipeRequest.getTemperature(),
                recipeRequest.getRatio(),
                recipeRequest.getDuration(),
                recipeRequest.getName(),
                uid,
                recipeRequest instanceof EspressoRecipe || recipeRequest.getIsEspresso()
        );
        
        // Convert to map for response
        Map<String, Object> recipeData = convertToMap(createdRecipe);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeData);
    }

    /**
     * Update an existing recipe by ID.
     * 
     * @param uid The unique identifier for the user owning the recipe
     * @param id The document ID of the recipe to update
     * @param requestBody Updated recipe data
     * @return ResponseEntity with updated recipe data
     */
    @PutMapping("users/{uid}/recipes/{id}")
    public ResponseEntity<Map<String, Object>> updateRecipe(
            @PathVariable String uid,
            @PathVariable String id,
            @RequestBody Recipe updatedRecipe) throws FileNotFoundException {
        
        // Find the existing recipe to verify it belongs to user
        Recipe existingRecipe = getRecipeById(id);
        if (existingRecipe == null || !uid.equals(existingRecipe.getUid())) {
            throw new FileNotFoundException("Recipe not found or access denied: " + id);
        }
        
        // Update only provided fields, keeping others unchanged
        existingRecipe.setName(updatedRecipe.getName() != null ? updatedRecipe.getName() : existingRecipe.getName());
        existingRecipe.setDescription(updatedRecipe.getDescription() != null ? updatedRecipe.getDescription() : existingRecipe.getDescription());
        existingRecipe.setTemperature(updatedRecipe.getTemperature() != 0 ? updatedRecipe.getTemperature() : existingRecipe.getTemperature());
        existingRecipe.setRatio(updatedRecipe.getRatio() != 0 ? updatedRecipe.getRatio() : existingRecipe.getRatio());
        existingRecipe.setDuration(updatedRecipe.getDuration() != 0 ? updatedRecipe.getDuration() : existingRecipe.getDuration());
        
        // Update water flow if provided
        if (updatedRecipe.getWaterMap() != null && !updatedRecipe.getWaterMap().isEmpty()) {
            existingRecipe.setWaterFlow(extractWaterMap(updatedRecipe.getWaterMap()));
        }
        
        Recipe updated = recipeService.saveRecipe(existingRecipe);
        return ResponseEntity.ok(convertToMap(updated));
    }

    /**
     * Delete a recipe by ID.
     * 
     * @param uid The unique identifier for the user owning the recipe
     * @param id The document ID of the recipe to delete
     */
    @DeleteMapping("users/{uid}/recipes/{id}")
    public void deleteRecipe(@PathVariable String uid, @PathVariable String id) throws FileNotFoundException {
        Recipe recipe = getRecipeById(id);
        
        if (recipe == null || !uid.equals(recipe.getUid())) {
            throw new FileNotFoundException("Recipe not found or access denied: " + id);
        }
        
        // Delete from repository
        recipeRepository.delete(recipe.getId());
    }

    /**
     * Get a single recipe by ID.
     * 
     * @param id The document ID of the recipe
     * @return ResponseEntity with recipe data or 404 if not found
     */
    @GetMapping("recipes/{id}")
    public ResponseEntity<Map<String, Object>> getRecipeById(@PathVariable String id) {
        try {
            Recipe recipe = recipeRepository.findById(id).orElse(null);
            
            if (recipe == null || recipe.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Recipe not found"));
            }
            
            return ResponseEntity.ok(convertToMap(recipe));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get recipes by type (Espresso or V60).
     * 
     * @param uid The unique identifier for the user
     * @param recipeType "ESPRESSO" or "V60"
     * @return Filtered list of recipe data maps
     */
    @GetMapping("users/{uid}/recipes/by-type/{type}")
    public List<Map<String, Object>> getRecipesByType(
            @PathVariable String uid,
            @PathVariable String type) {
        
        return getUserRecipes(uid).stream()
                .filter(map -> type.equals(type))
                .toList();
    }

    // Helper methods
    
    /**
     * Get all recipes by converting repository results.
     */
    private List<Recipe> getRecipesByUser(String uid) throws FileNotFoundException {
        return recipeRepository.findAll().stream()
                .filter(recipe -> uid.equals(recipe.getUid()))
                .toList();
    }

    /**
     * Get a single recipe by ID.
     */
    @Autowired
    private com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.RecipeRepository recipeRepository;
    
    private Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    /**
     * Convert Recipe object to Map for JSON response.
     */
    private Map<String, Object> convertToMap(Recipe recipe) {
        Map<String, Object> map = new HashMap<>();
        
        if (recipe.getId() != null) map.put("id", recipe.getId());
        if (recipe.getName() != null) map.put("name", recipe.getName());
        if (recipe.getUid() != null) map.put("uid", recipe.getUid());
        if (recipe.getDescription() != null) map.put("description", recipe.getDescription());
        if (recipe.getTemperature() != 0) map.put("temperature", recipe.getTemperature());
        if (recipe.getRatio() != 0) map.put("ratio", recipe.getRatio());
        if (recipe.getDuration() != 0) map.put("duration", recipe.getDuration());
        
        // Add water flow information based on recipe type
        if (recipe instanceof EspressoRecipe) {
            List<com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water> flows = 
                    ((EspressoRecipe) recipe).getWaterFlow();
            map.put("waterFlows", flows.stream().map(w -> createWaterMap(w)).toList());
        } else if (recipe instanceof V60Recipe) {
            List<com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water> amounts = 
                    ((V60Recipe) recipe).getWaterAmount();
            map.put("waterAmounts", amounts.stream().map(w -> createWaterMap(w)).toList());
        }
        
        map.put("type", recipe instanceof EspressoRecipe ? "Espresso" : "V60");
        
        return map;
    }

    /**
     * Extract water maps from list of Water objects.
     */
    private List<Map<String, Object>> extractWaterMap(List<com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water> waters) {
        if (waters == null || waters.isEmpty()) {
            return List.of();
        }
        
        return waters.stream().map(this::createWaterMap).toList();
    }

    /**
     * Convert Water object to map.
     */
    private Map<String, Object> createWaterMap(com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water water) {
        Map<String, Object> map = new HashMap<>();
        if (water.getName() != null) map.put("brewPhase", water.getName());
        if (water.getTime() != 0) map.put("time", water.getTime());
        return map;
    }

}