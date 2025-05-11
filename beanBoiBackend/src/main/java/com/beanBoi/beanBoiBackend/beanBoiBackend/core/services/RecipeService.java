package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.EspressoRecipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Recipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.V60Recipe;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Water;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(List<Map<String, Object>> waterMap, String description, double temperature, double ratio, double duration, String name, String uid, boolean isEspresso) {
        if (isEspresso) {
            EspressoRecipe recipe = new EspressoRecipe();
            List<Water> waterList = waterMap.stream().map(water ->
            {
                Water waterObj = new Water();
                waterObj.setName(water.get("brewPhase").toString());
                waterObj.setTime(Double.parseDouble(water.get("time").toString()));
                waterObj.setWater(Double.parseDouble(water.get("waterFlow").toString()));
                return waterObj;
            }).toList();
            recipe.setWaterFlow(waterList);
            setCommonAttributes(temperature, ratio, duration, name, recipe, description, uid);
            recipeRepository.saveDocument(recipe);
            return recipe;
        } else {
            V60Recipe recipe = new V60Recipe();
            List<Water> waterList = waterMap.stream().map(water ->
            {
                Water waterObj = new Water();
                waterObj.setName(water.get("brewPhase").toString());
                waterObj.setTime(Double.parseDouble(water.get("time").toString()));
                waterObj.setWater(Double.parseDouble(water.get("waterAmount").toString()));
                return waterObj;
            }).toList();
            recipe.setWaterAmount(waterList);
            setCommonAttributes(temperature, ratio, duration, name, recipe, description, uid);
            recipeRepository.saveDocument(recipe);
            return recipe;
        }
    }

    private void setCommonAttributes(double temperature, double ratio, double duration, String name, Recipe recipe,  String description, String uid) {
        recipe.setTemperature(temperature);
        recipe.setRatio(ratio);
        recipe.setDuration(duration);
        recipe.setName(name);
        recipe.setUid(uid);
    }



}
