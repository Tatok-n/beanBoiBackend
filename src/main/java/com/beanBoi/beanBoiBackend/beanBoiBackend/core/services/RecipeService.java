package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.*;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

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
            recipeRepository.save(recipe);
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
            recipeRepository.save(recipe);
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

    public List<Recipe> getRecipesForUser(String uid){
        return recipeRepository.findAllByUid(uid).stream().filter(DocumentData::isActive).toList();
    }

    public List<Recipe> getAllRecipesForUser(String uid){
        return recipeRepository.findAllByUid(uid);
    }

    public EspressoRecipe updateEspressoRecipe(EspressoRecipe recipe) throws FileNotFoundException {
        EspressoRecipe old_recipe = (EspressoRecipe) recipeRepository.findById(recipe.getId()).orElseThrow(FileSystemNotFoundException::new);
        old_recipe.setName(recipe.getName());
        old_recipe.setRatio(recipe.getRatio());
        old_recipe.setUid(recipe.getUid());
        old_recipe.setTemperature(recipe.getTemperature());
        old_recipe.setWaterFlow(recipe.getWaterFlow());
        old_recipe.setDescription(recipe.getDescription());
        old_recipe.setActive(recipe.isActive());
        recipeRepository.save(old_recipe);
        return old_recipe;
    }

    public V60Recipe updateV60Recipe(V60Recipe recipe) throws FileNotFoundException {
        V60Recipe old_recipe = (V60Recipe) recipeRepository.findById(recipe.getId()).orElseThrow(FileSystemNotFoundException::new);
        old_recipe.setName(recipe.getName());
        old_recipe.setRatio(recipe.getRatio());
        old_recipe.setUid(recipe.getUid());
        old_recipe.setTemperature(recipe.getTemperature());
        old_recipe.setWaterAmount(recipe.getWaterAmount());
        old_recipe.setDescription(recipe.getDescription());
        old_recipe.setActive(recipe.isActive());
        recipeRepository.save(old_recipe);
        recipeRepository.save(old_recipe);
        return old_recipe;
    }

    public void deactivateRecipe(String id) throws FileNotFoundException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(FileNotFoundException::new);
        recipe.setActive(false);
        recipeRepository.save(recipe);
    }

    public Recipe findRecipeById(String id) throws FileNotFoundException{
        return recipeRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }







}
