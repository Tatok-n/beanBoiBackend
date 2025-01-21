package com.beanBoi.beanBoiBackend.beanBoiBackend.repositories;

import com.beanBoi.beanBoiBackend.beanBoiBackend.models.*;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RecipeRepository extends DocumentRepository{

    private final FirestoreImplementation firestoreImplementation;

    public RecipeRepository(FirestoreImplementation firestoreImplementation) {
        this.collectionName = "recipes";
        this.firestoreImplementation = firestoreImplementation;
    }

    @Override
    public Map<String, Object> getAsMap(DocumentData data) {
        Recipe recipe = (Recipe) data;
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("name", recipe.getName());
        recipeMap.put("description", recipe.getDescription());
        recipeMap.put("duration", recipe.getDuration());
        recipeMap.put("ratio", String.valueOf(recipe.getDuration()));
        recipeMap.put("temperature", String.valueOf(recipe.getTemperature()));
        recipeMap.put("isActive", recipe.isActive());

        if (recipe.getClass().equals(EspressoRecipe.class)) {
            EspressoRecipe eRecipe = (EspressoRecipe) recipe;
            recipeMap.put("type", "Espresso");
            List<Map <String,Object>> waterFlow = eRecipe.getWaterFlow().stream().map(water -> {
                Map<String, Object> waterMap = new HashMap<>();
                waterMap.put("flow", String.valueOf(water.getWater()));
                waterMap.put("time", water.getTime());
                return waterMap;
            }).toList();
            recipeMap.put("water flow", waterFlow);
        } else {
            V60Recipe vRecipe = (V60Recipe) recipe;
            recipeMap.put("type", "V60");
            List<Map <String,Object>> waterAmount = vRecipe.getWaterAmount().stream().map(water -> {
                Map<String, Object> waterMap = new HashMap<>();
                waterMap.put("amount", String.valueOf(water.getWater()));
                waterMap.put("time", water.getTime());
                return waterMap;
            }).toList();
            recipeMap.put("water amount", waterAmount);
        }
        return recipeMap;
    }

    @Override
    public DocumentData getFromMap(Map<String, Object> map) {

        Recipe recipe = new Recipe();
        recipe.setName((String) map.get("name"));
        recipe.setUid((String) map.get("uid"));
        recipe.setDescription((String) map.get("description"));
        recipe.setDuration((Long) map.get("duration"));
        recipe.setRatio(Float.parseFloat(map.get("duration").toString()));
        recipe.setActive((Boolean) map.get("isActive"));
        recipe.setTemperature(Float.parseFloat(map.get("temperature").toString()));

        if (map.get("type").equals("Espresso")) {
            List<Map<String, Object>> waterFlow = (List<Map<String, Object>>) map.get("water flow");
            List<Water> waterList =  waterFlow.stream().map(waterMap -> {
                Water water = new Water();
                water.setTime((Long) waterMap.get("time"));
                water.setWater(Float.parseFloat(waterMap.get("flow").toString()));
                return water;
            }).toList();
            EspressoRecipe espressoRecipe = getEspressoRecipeFromRecipe(recipe);
            espressoRecipe.setWaterFlow(waterList);
            return espressoRecipe;

        } else {
            List<Map<String, Object>> waterAmount = (List<Map<String, Object>>) map.get("water amount");
            List<Water> waterList = waterAmount.stream().map(waterMap -> {
                Water water = new Water();
                water.setTime((Long) waterMap.get("time"));
                water.setWater(Float.parseFloat(waterMap.get("amount").toString()));
                return water;
            }).toList();
            V60Recipe v60Recipe = getV60RecipeFromRecipe(recipe);
            v60Recipe.setWaterAmount(waterList);
            return v60Recipe;
        }
    }

    public Recipe getRecipeById(String id) {
        return (Recipe) getDocumentById(id);
    }

    private EspressoRecipe getEspressoRecipeFromRecipe(Recipe recipe) {
        EspressoRecipe eRecipe = new EspressoRecipe();
        eRecipe.setName(recipe.getName());
        eRecipe.setDescription(recipe.getDescription());
        eRecipe.setDuration(recipe.getDuration());
        eRecipe.setRatio(recipe.getRatio());
        eRecipe.setActive(recipe.isActive());
        eRecipe.setTemperature(recipe.getTemperature());
        eRecipe.setUid(recipe.getUid());
        return eRecipe;
    }

    private V60Recipe getV60RecipeFromRecipe(Recipe recipe) {
        V60Recipe vRecipe = new V60Recipe();
        vRecipe.setName(recipe.getName());
        vRecipe.setDescription(recipe.getDescription());
        vRecipe.setDuration(recipe.getDuration());
        vRecipe.setRatio(recipe.getRatio());
        vRecipe.setActive(recipe.isActive());
        vRecipe.setTemperature(recipe.getTemperature());
        vRecipe.setUid(recipe.getUid());
        return vRecipe;
    }

    public Recipe verifyRecipe(DocumentReference reference) {
        Recipe recipe = new Recipe();
        if (firestoreImplementation.getDocumentFromReference(reference).getData() == null) {
            recipe.setDuration(-1f);
        } else {
            recipe = getRecipeById(firestoreImplementation.getDocumentFromReference(reference).getId());
        }
        return recipe;
    }


}
